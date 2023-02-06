import mxgraph from '_com/MxGraph/index';
import X2js from 'x2js';
// import Shapes from './Shapes';
// import { init } from 'echarts';
// import _ from 'lodash';

const { mxConstants } = mxgraph;

export function generateAction(currentTaskId, models, modelInputInGraph, modelLinkInGraph, modelOutputInGraph, linkEdgeList, type) {
  let action = {
    taskId: currentTaskId,
    modelItemList: [],
    dataItemList: [],
    parameterList: [],
    conditionList: [],
    dependencyList: []
  };

  //get same model input & output
  let allInputList = [...modelInputInGraph, ...modelLinkInGraph];
  action.modelItemList = generateModelItemList(models, allInputList, modelOutputInGraph, linkEdgeList);
  //dataItemList
  if (type == 'task') {
    action.dataItemList = generateInputDataItemList(modelInputInGraph);
  }
  if (type == 'taskInstance') {
    action.dataItemList = [...generateInputDataItemList(modelInputInGraph), ...generateOutputDataItemList(modelOutputInGraph)];
  }

  action.dependencyList = generateDependencyList(linkEdgeList);
  return action;
}

function generateModelItemList(models, allInputList, modelOutputInGraph, linkEdgeList) {
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
    let outputList = modelOutputInGraph.filter(event => event.md5 == model.md5);

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

function generateInputDataItemList(modelInputInGraph) {
  let inputList = modelInputInGraph.filter(event => Object.prototype.hasOwnProperty.call(event, 'value') && event.value != '' && event.value != undefined);
  let dataItemList = [];

  inputList.forEach(item => {
    let inputData = item.dataResourceRelated;

    dataItemList.push(inputData);
  });

  return dataItemList;
}

function generateOutputDataItemList(modelOutputInGraph) {
  let outputList = modelOutputInGraph.filter(event => Object.prototype.hasOwnProperty.call(event, 'value') && event.value != '' && event.value != undefined);
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

export function generateXml(
  taskName,
  modelListInGraph,
  modelInputInGraph,
  modelLinkInGraph,
  modelOutputInGraph,
  linkEdgeList,
  dataServiceListInGraph,
  dataServiceInputInGraph,
  dataServiceLinkInGraph,
  dataServiceOutputListInGraph
) {
  let version = '1.0';
  let uid = generateGUID();
  let name = taskName;

  let xml = `<TaskConfiguration uid='${uid}' name='${name}' version='${version}'>`;

  xml += '<Models>';

  //没有md5-->只有doi  xml += `<Model name='${model.name}' pid='${model.md5}' description='' doi='${model.doi}'/>`;
  modelListInGraph.forEach(model => {
    xml += `<Model name='${model.name}' description='' pid='${model.nodeAttribute.md5}'/>`;
  });
  xml += `</Models>`;

  //modelAction标签
  xml += `<ModelActions>`;

  modelListInGraph.forEach(model => {
    xml += `<ModelAction id='${model.id}' name = '${model.name}' description = ''
    model='${model.nodeAttribute.md5}' iterationNum='${model.iterationNum}'>`;

    let list = [...modelInputInGraph, ...modelLinkInGraph];
    let inputList = list.filter(event => event.nodeAttribute.md5 == model.nodeAttribute.md5);
    let outputList = modelOutputInGraph.filter(event => event.nodeAttribute.md5 == model.nodeAttribute.md5);

    xml += `<Inputs>`;
    inputList.forEach(item => {
      xml += `<DataConfiguration id='${item.nodeAttribute.eventId}' state='${item.nodeAttribute.stateName}' event='${item.name}'>`;

      if (item.type == 'modelServiceInput') {
        xml += `<Data value='${item.dataResourceRelated.value}' type="url"/>`;
      } else if (item.type == 'modelServiceLink') {
        let link = linkEdgeList.filter(el => el.target.nodeAttribute.eventId == item.nodeAttribute.eventId);
        xml += `<Data link='${link[0].source.nodeAttribute.eventId}' type="link"/>`;
      }
      xml += `</DataConfiguration>`;
    });

    xml += `</Inputs>`;
    xml += `<Outputs>`;

    outputList.forEach(item => {
      xml += `<DataConfiguration id='${item.nodeAttribute.eventId}' state='${item.nodeAttribute.stateName}' event='${item.name}' />`;
    });

    xml += '</Outputs></ModelAction>';
  });

  //data service
  //data service
  //data service
  //data service
  //data service
  xml += '</ModelActions><ProcessingTools>';
  dataServiceListInGraph.forEach(service => {
    xml += `<ProcessingTool name='${service.name}' description='${service.description}' service='${service.nodeAttribute.dataServiceId}'  source='internal'/>`;
  });
  xml += `</ProcessingTools><DataProcessings>`;

  dataServiceListInGraph.forEach(service => {
    xml += `<DataProcessing id='${service.id}' token='${service.nodeAttribute.token}' name = '${service.name}' description = ''
    type='dataService' service='${service.nodeAttribute.dataServiceId}'>`;

    let list = [...dataServiceInputInGraph, ...dataServiceLinkInGraph];
    let inputList = list.filter(event => event.nodeAttribute.dataServiceId == service.nodeAttribute.dataServiceId);
    let outputList = dataServiceOutputListInGraph.filter(event => event.nodeAttribute.dataServiceId == service.nodeAttribute.dataServiceId);

    xml += `<Inputs>`;
    inputList.forEach(item => {
      xml += `<DataConfiguration id='${item.id}'  state='${item.nodeAttribute.stateName}' event='${item.name}'>`;

      if (item.type == 'dataServiceInput') {
        xml += `<Data value='${item.dataResourceRelated.value}' type="url"/>`;
      } else if (item.type == 'dataServiceLink') {
        let link = linkEdgeList.filter(el => el.target.id == item.id);
        xml += `<Data link='${link[0].source.id}' type="link"/>`;
      }
      xml += `</DataConfiguration>`;
    });

    xml += `</Inputs>`;
    xml += `<Outputs>`;

    outputList.forEach(item => {
      xml += `<DataConfiguration id='${item.id}' state='${item.nodeAttribute.stateName}' event='${item.name}' />`;
    });

    xml += '</Outputs></DataProcessing>';
  });
  xml += '</DataProcessings>';

  xml += '</TaskConfiguration>';
  console.log(xml);
  return xml;
}

export function generateXml1(
  taskName,
  modelListInGraph,
  modelInputInGraph,
  modelLinkInGraph,
  modelOutputInGraph,
  linkEdgeList,
  dataServiceListInGraph,
  dataServiceInputInGraph,
  dataServiceLinkInGraph,
  dataServiceOutputListInGraph
) {
  console.log(taskName);
  console.log(modelListInGraph);
  console.log(modelInputInGraph);
  console.log(modelLinkInGraph);
  console.log(modelOutputInGraph);
  console.log(linkEdgeList);
  console.log(dataServiceListInGraph);
  console.log(dataServiceInputInGraph);
  console.log(dataServiceLinkInGraph);
  console.log(dataServiceOutputListInGraph);
  let jsonObj = {
    TaskConfiguration: {
      _uid: generateGUID(),
      _name: taskName,
      _version: '1.0'
    }
  };

  let Models = {
    Model: []
  };
  modelListInGraph.forEach(model => {
    Models.Model.push({
      _name: model.name,
      _description: '',
      _pid: model.nodeAttribute.md5
    });
  });

  let ModelActions = {
    ModelAction: []
  };
  modelListInGraph.forEach((model, index) => {
    let inputList = [];
    let outputList = [];
    model.edges.forEach(edge => {
      if (edge.target.id == model.id) {
        inputList.push(edge.source);
      }
      if (edge.source.id == model.id) {
        outputList.push(edge.target);
      }
    });
    console.log(inputList);
    ModelActions.ModelAction.push({
      _id: model.id,
      _name: model.name,
      _description: '',
      _model: model.nodeAttribute.md5,
      _iterationNum: model.iterationNum,
      _step: index,
      Inputs: {
        DataConfiguration: []
      },
      Outputs: {
        DataConfiguration: []
      }
    });
    inputList.forEach(item => {
      if (item.type == 'modelServiceInput') {
        let type = 'url';
        if (item.nodeAttribute.dataSelect.type == 'shared_file') {
          type = 'insituData';
          ModelActions.ModelAction[index].Inputs.DataConfiguration.push({
            _id: item.id,
            _state: item.nodeAttribute.stateName,
            _event: item.nodeAttribute.eventName,
            Data: {
              _value: item.nodeAttribute.dataSelect.value,
              _type: type
            }
          });
        }
        if (item.nodeAttribute.dataSelect.type == 'parameter') {
          ModelActions.ModelAction[index].Inputs.DataConfiguration.push({
            _id: item.id,
            _state: item.nodeAttribute.stateName,
            _event: item.nodeAttribute.eventName,
            Data: {
              _value: item.nodeAttribute.dataSelect.value,
              _type: type
            }
          });
        } else {
          ModelActions.ModelAction[index].Inputs.DataConfiguration.push({
            _id: item.id,
            _state: item.nodeAttribute.stateName,
            _event: item.nodeAttribute.eventName,
            Data: {
              _value: item.nodeAttribute.dataSelect.value,
              _type: type
            }
          });
        }
      } else if (item.type == 'modelServiceLink') {
        console.log(1);
        let link = {};
        item.edges.forEach(edge => {
          if (edge.target.id == item.id) {
            link = edge.source;
          }
        });
        ModelActions.ModelAction[index].Inputs.DataConfiguration.push({
          _id: item.id,
          _state: item.nodeAttribute.stateName,
          _event: item.nodeAttribute.eventName,
          Data: {
            _link: link.id,
            _type: 'link'
          }
        });
      }
    });
    outputList.forEach(item => {
      ModelActions.ModelAction[index].Outputs.DataConfiguration.push({
        _id: item.id,
        _state: item.nodeAttribute.stateName,
        _event: item.nodeAttribute.eventName
      });
    });
  });

  let ProcessingTools = {
    ProcessingTool: []
  };
  dataServiceListInGraph.forEach(service => {
    ProcessingTools.ProcessingTool.push({
      _name: service.name,
      _description: service.description,
      _service: service.nodeAttribute.dataServiceId,
      _source: 'internal'
    });
  });

  let DataProcessings = {
    DataProcessing: []
  };
  dataServiceListInGraph.forEach((service, index) => {
    // let list = [...dataServiceInputInGraph, ...dataServiceLinkInGraph];
    let inputList = [];
    let outputList = [];
    service.edges.forEach(edge => {
      if (edge.target.id == service.id) {
        inputList.push(edge.source);
      }
      if (edge.source.id == service.id) {
        outputList.push(edge.target);
      }
    });

    console.log(inputList);
    DataProcessings.DataProcessing.push({
      _id: service.id,
      _token: service.nodeAttribute.token,
      _name: service.name,
      _description: '',
      _type: 'dataService',
      _service: service.nodeAttribute.dataServiceId,
      Inputs: {
        DataConfiguration: []
      },
      Outputs: {
        DataConfiguration: []
      }
    });
    inputList.forEach(item => {
      if (item.type == 'dataServiceInput') {
        if (item.nodeAttribute.dataSelect.type == 'shared_file') {
          DataProcessings.DataProcessing[index].Inputs.DataConfiguration.push({
            _id: item.id,
            _event: item.name,
            Data: {
              _value: item.nodeAttribute.dataSelect.value,
              _type: 'insituData'
            }
          });
        }
        if (item.nodeAttribute.dataSelect.type == 'parameter') {
          DataProcessings.DataProcessing[index].Inputs.DataConfiguration.push({
            _id: item.id,
            _event: item.name,
            Data: {
              _value: item.nodeAttribute.dataSelect.value,
              _type: 'param'
            }
          });
        } else {
          console.log(1);
          DataProcessings.DataProcessing[index].Inputs.DataConfiguration.push({
            _id: item.id,
            _event: item.name,
            Data: {
              _value: item.nodeAttribute.dataSelect.value,
              _type: 'url'
            }
          });
        }
      } else if (item.type == 'dataServiceLink') {
        let link = {};
        item.edges.forEach(edge => {
          if (edge.target.id == item.id) {
            link = edge.source;
          }
        });
        DataProcessings.DataProcessing[index].Inputs.DataConfiguration.push({
          _id: item.id,
          _event: item.name,
          Data: {
            _link: link.id,
            _type: 'link'
          }
        });
      }
    });
    outputList.forEach((item, n) => {
      DataProcessings.DataProcessing[index].Outputs.DataConfiguration.push({
        _id: item.id,
        _event: item.name
      });
      if (!item.nodeAttribute.upload) {
        DataProcessings.DataProcessing[index].Outputs.DataConfiguration[n].Data = {
          _type: 'insituData'
        };
      }
    });
  });
  let DataLinks = {
    DataLink: []
  };
  modelLinkInGraph.forEach(item => {
    item.edges.forEach(edge => {
      if (edge.target.id == item.id) {
        DataLinks.DataLink.push({
          _from: edge.source.id,
          _to: item.id
        });
      }
    });
  });

  if (Models.Model.length > 0) {
    jsonObj.TaskConfiguration.Models = Models;
  }
  if (ModelActions.ModelAction.length > 0) {
    jsonObj.TaskConfiguration.ModelActions = ModelActions;
  }
  if (ProcessingTools.ProcessingTool.length > 0) {
    jsonObj.TaskConfiguration.ProcessingTools = ProcessingTools;
  }
  if (DataProcessings.DataProcessing.length > 0) {
    jsonObj.TaskConfiguration.DataProcessings = DataProcessings;
  }
  if (DataLinks.DataLink.length > 0) {
    jsonObj.TaskConfiguration.DataLinks = DataLinks;
  }
  let x2js = new X2js();
  // console.log(x2js.js2xml(jsonObj))
  return x2js.js2xml(jsonObj);
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
  if (type == 'modelService') {
    return {
      fontColor: '#f6f6f6',
      fillColor: '#07689f',
      strokeColor: '',
      shape: 'rectangle'
    };
  }
  if (type == 'dataService') {
    return {
      fontColor: '#f6f6f6',
      fillColor: '#00FFF8',
      strokeColor: '',
      shape: 'rectangle'
    };
  }
  if (type == 'modelServiceInput' || type == 'dataServiceInput') {
    return {
      fillColor: '#FFF0F0',
      fontColor: '#24292E',
      strokeColor: '',
      shape: 'parallelogram',
      fixedSize: 1
    };
  }
  if (type == 'parameter') {
    return {
      fillColor: '#F497E8',
      fontColor: '#24292E',
      strokeColor: '',
      shape: 'parallelogram',
      fixedSize: 1
    };
  }
  if (type == 'modelServiceLink' || type == 'dataServiceLink') {
    return {
      // fillColor: 'rgb(255, 220, 220)',
      fillColor: '#FFF0F0',

      fontColor: '#24292E',
      strokeColor: '',
      shape: 'parallelogram',
      fixedSize: 1
    };
  }
  if (type == 'modelServiceOutput' || type == 'dataServiceOutput') {
    return {
      fillColor: '#b9e6d3', //b9e6d3 f4d160
      fontColor: '#24292E',
      strokeColor: '',
      shape: 'parallelogram'
    };
  }
  if (type == 'codeBar') {
    return {
      fontColor: '#f6f6f6',
      fillColor: '#c0d7eb',
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
