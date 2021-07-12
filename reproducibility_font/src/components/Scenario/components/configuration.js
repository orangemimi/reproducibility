import mxgraph from '_com/MxGraph/index';
// import Shapes from './Shapes';
// import { init } from 'echarts';
// import _ from 'lodash';

const { mxConstants } = mxgraph;

export function generateAction(currentTaskId, models, dataInputInGraph, dataLinkInGraph, dataOutputInGraph, linkEdgeList, type) {
  let action = {
    taskId: currentTaskId,
    modelItemList: [],
    dataItemList: [],
    parameterList: [],
    conditionList: [],
    dependencyList: []
  };

  //get same model input & output
  let allInputList = [...dataInputInGraph, ...dataLinkInGraph];
  action.modelItemList = generateModelItemList(models, allInputList, dataOutputInGraph, linkEdgeList);
  //dataItemList
  if (type == 'task') {
    action.dataItemList = generateInputDataItemList(dataInputInGraph);
  }
  if (type == 'taskInstance') {
    action.dataItemList = [...generateInputDataItemList(dataInputInGraph), ...generateOutputDataItemList(dataOutputInGraph)];
  }

  action.dependencyList = generateDependencyList(linkEdgeList);
  return action;
}

function generateModelItemList(models, allInputList, dataOutputInGraph, linkEdgeList) {
  let modelList = [];
  //拼接集成模型中的models部分
  // debugger;

  models.forEach(model => {
    let modelItem = {
      id: model.id,
      // modelOid: model.doi,
      reference: model.md5,
      name: model.name,
      description: model.description,
      behavior: {}
    };

    let inputList = allInputList.filter(event => event.md5 == model.md5);
    let outputList = dataOutputInGraph.filter(event => event.md5 == model.md5);

    let stateIdList = [];
    if (inputList.length != 0) {
      inputList.forEach(input =>
        stateIdList.push({
          id: input.stateId,
          name: input.stateName,
          description: input.stateDescription
        })
      );
    }
    //获得所有的状态
    let stateIdListNew = stateIdList.reduce((all, next) => (all.some(item => item.id == next.id) ? all : [...all, next]), []);

    // let stateList = [];

    stateIdListNew.forEach(state => {
      let eventList = [];

      let inputInState = inputList.filter(el => el.stateId == state.id);
      let outputInState = outputList.filter(el => el.stateId == state.id);
      inputInState.forEach(item => {
        let inputData = {
          id: item.id,
          reference: item.eventId,
          name: item.name,
          description: item.description,
          optional: item.optional,
          type: item.type
        };
        if (item.type == 'input') {
          inputData.dataRef = item.fileId;
        } else if (item.type == 'link') {
          // let link = linkEdgeList.filter(el => el.target.eventId == item.eventId);

          let link = linkEdgeList.filter(link => link.target.id == item.id);
          // inputData.link = link[0].source.eventId; //if type ==link, dataRef = 上一个output的
          inputData.dataRef = link[0].source.fileId;
        }

        eventList.push(inputData);
      });

      outputInState.forEach(item => {
        let output = {
          id: item.id,
          reference: item.eventId,
          name: item.name,
          description: item.description,
          optional: item.optional,
          type: item.type
        };
        if (Object.prototype.hasOwnProperty.call(item, 'fileId')) {
          output.dataRef = item.fileId;
        } else {
          output.dataRef = '';
        }
        eventList.push(output);
      });

      state.eventList = eventList;
    });

    modelItem.behavior.stateList = stateIdListNew;
    // $set(modelItem, 'stateList', stateList);
    modelList.push(modelItem);
  });
  return modelList;
}

function generateInputDataItemList(dataInputInGraph) {
  let inputList = dataInputInGraph.filter(event => Object.prototype.hasOwnProperty.call(event, 'value') && event.value != '' && event.value != undefined);
  let dataItemList = [];

  inputList.forEach(item => {
    let inputData = {
      id: item.fileId,
      name: item.fileName,
      description: item.fileDescription,
      value: item.value
    };

    dataItemList.push(inputData);
  });

  return dataItemList;
}

function generateOutputDataItemList(dataOutputInGraph) {
  let outputList = dataOutputInGraph.filter(event => Object.prototype.hasOwnProperty.call(event, 'value') && event.value != '' && event.value != undefined);
  let dataItemList = [];

  outputList.forEach(item => {
    let output = {
      id: item.fileId,
      name: item.fileName,
      description: item.description,
      value: item.value //如果是task value =""/如果是instance value ="item.value"
    };
    dataItemList.push(output);
  });
  return dataItemList;
}

function generateDependencyList(linkEdgeList) {
  let linkList = linkEdgeList;
  let dependencyList = [];

  linkList.forEach(edge => {
    let dependency = {};
    const beforeCell = edge.source;
    const nextCell = edge.target;

    let from, to;

    if (beforeCell.type == 'output' || beforeCell.type == 'input' || beforeCell.type == 'link') {
      from = {
        type: 'data',
        id: beforeCell.id,
        reference: beforeCell.eventId
      };
    } else {
      from = {
        type: 'model',
        id: beforeCell.id,
        reference: beforeCell.md5
      };
    }

    if (nextCell.type == 'output' || nextCell.type == 'input' || nextCell.type == 'link') {
      to = {
        type: 'data',
        id: nextCell.id,
        reference: nextCell.eventId
      };
    } else {
      to = {
        type: 'model',
        id: nextCell.id,
        reference: nextCell.md5
      };
    }

    dependency = {
      id: edge.id,
      name: `${from.type}: ${beforeCell.name} to ${to.type}: ${nextCell.name} denpendency`,
      from: from,
      to: to
    };

    dependencyList.push(dependency);
  });
}

export function generateXml(taskName, modelListInGraph, dataInputInGraph, dataLinkInGraph, dataOutputInGraph, linkEdgeList) {
  let version = '1.0';
  let uid = generateGUID();
  let name = taskName;

  let xml = `<TaskConfiguration uid='${uid}' name='${name}' version='${version}'>`;

  xml += '<Models>';

  //没有md5-->只有doi  xml += `<Model name='${model.name}' pid='${model.md5}' description='' doi='${model.doi}'/>`;
  modelListInGraph.forEach(model => {
    xml += `<Model name='${model.name}' description='' pid='${model.md5}'/>`;
  });
  xml += `</Models>`;

  //modelAction标签
  xml += `<ModelActions>`;

  modelListInGraph.forEach(model => {
    xml += `<ModelAction id='${model.id}' name = '${model.name}' description = ''
    model='${model.md5}' iterationNum='${model.iterationNum}'>`;

    let list = [...dataInputInGraph, ...dataLinkInGraph];
    let inputList = list.filter(event => event.md5 == model.md5);
    let outputList = dataOutputInGraph.filter(event => event.md5 == model.md5);

    xml += `<Inputs>`;
    inputList.forEach(item => {
      xml += `<DataConfiguration id='${item.eventId}' state='${item.stateName}' event='${item.name}'>`;

      if (item.type == 'input') {
        xml += `<Data value='${item.value}' type="url"/>`;
      } else if (item.type == 'link') {
        let link = linkEdgeList.filter(el => el.target.eventId == item.eventId);
        xml += `<Data link='${link[0].source.eventId}' type="link"/>`;
      }
      xml += `</DataConfiguration>`;
    });

    xml += `</Inputs>`;
    xml += `<Outputs>`;

    outputList.forEach(item => {
      xml += `<DataConfiguration id='${item.eventId}' state='${item.stateName}' event='${item.name}' />`;
    });

    xml += '</Outputs></ModelAction>';
  });
  xml += '</ModelActions></TaskConfiguration>';
  console.log(xml);
  return xml;
}

function generateGUID() {
  let s = [];
  let hexDigits = '0123456789abcdef';
  for (let i = 0; i < 36; i++) {
    s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
  }
  s[14] = '4'; // bits 12-15 of the time_hi_and_version field to 0010
  s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1); // bits 6-7 of the clock_seq_hi_and_reserved to 01
  s[8] = s[13] = s[18] = s[23] = '-';
  let uuid = s.join('');
  return uuid;
}

export function differCellStyle(type) {
  if (type == 'modelBar') {
    return {
      fontColor: '#f6f6f6',
      fillColor: '#07689f',
      strokeColor: '',
      shape: 'rectangle'
    };
  }
  if (type == 'inputBar') {
    return {
      fillColor: '#fff8f8',
      fontColor: '#24292E',
      strokeColor: '',
      shape: 'parallelogram',
      fixedSize: 1
    };
  }
  if (type == 'link') {
    return {
      fillColor: 'rgb(255, 220, 220)',
      fontColor: '#24292E',
      strokeColor: '',
      shape: 'parallelogram',
      fixedSize: 1
    };
  }
  if (type == 'outputBar') {
    return {
      fillColor: '#b9e6d3', //b9e6d3 f4d160
      fontColor: '#24292E',
      strokeColor: '',
      shape: 'parallelogram'
    };
  }
  if (type == 'codeBar') {
    return {
      fillColor: '#83b5e1', //b9e6d3 f4d160
      fontColor: '#24292E',
      strokeColor: '',
      shape: 'rectangle'
    };
  }
  return {
    fillColor: '#b9e6d3', //b9e6d3 f4d160
    fontColor: '#24292E',
    strokeColor: '',
    shape: 'rhombus'
  };
}

export function getCellStyle(styleIn, cell) {
  // console.log(item);
  let styleObj = {
    ...styleIn,
    strokeWidth: '1.5',
    align: mxConstants.ALIGN_CENTER,
    // verticalAlign: mxConstants.ALIGN_,
    imageAlign: mxConstants.ALIGN_CENTER,
    imageVerticalAlign: mxConstants.ALIGN_TOP
  };

  if (cell.optional == 'false' && (cell.type == 'input' || cell.type == 'link')) {
    styleObj.strokeColor = '#d13030';
  }

  //转换成cell中的style格式
  const style = Object.keys(styleObj)
    .map(attr => `${attr}=${styleObj[attr]}`)
    .join(';');

  return style;
}
