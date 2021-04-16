export function generateAction(currentTask, models, dataInputInGraph, dataLinkInGraph, dataOutputInGraph, linkEdgeList, type) {
  let action = {
    taskId: currentTask.id,
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
