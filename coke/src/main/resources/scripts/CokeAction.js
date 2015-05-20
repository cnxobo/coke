/**
 * 
 */

function $xa_insertItem(dataSet, dataPath, dialog, data) {
	if (!data) {
		data = {
			xybz : true,
			yxbz : true
		};
	}
	dataSet.getData(dataPath).insert(data);
	if (dialog) {
		dialog.show();
	}
}

function $xa_insertChildItem(dataTree, childrenName, dialog, data) {
	childrenName = childrenName || "children";
	data = data || {};

	var currentEntity = dataTree.get("currentEntity");
	if (currentEntity) {
		dataTree.get("currentNode").expand();
		// newEntity = currentEntity.createChild(childrenName, data);
		newEntity = currentEntity.get("children").insert(data);
		dataTree.set("currentEntity", newEntity);
		setTimeout(function() {
			dataTree.set("currentEntity", newEntity);
		}, 200);
		if (dialog) {
			dialog.show();
		}
	}
}

function $xa_isItemEditable(toolbarEditId) {
	if (toolbarEditId) {
		var toolbarEdit = view.id(toolbarEditId);
		return toolbarEdit && !toolbarEdit.get("disabled");
	} else {
		return true;
	}
}

function $xa_editItem(dataSet, dataPath, dialog, toolbarEditId) {
	if ($xa_isItemEditable(toolbarEditId)) {
		var entity = dataSet.getData(dataPath);
		if (entity) {
			dialog.show();
		} else {
			dorado.widget.NotifyTipManager.notify("没有可编辑的记录!");
		}
	}
}

function $xa_deleteItem(dataSet, dataPath, updateAction, callBack) {
	var entity = dataSet.getData(dataPath);
	if (entity) {
		dorado.MessageBox.confirm("确认要删除选中的记录么？", {
			icon : "WARNING",
			title : "删除记录",
			callback : function() {
				entity.remove();
				updateAction.execute(callBack);
			}
		});
	} else {
		dorado.widget.NotifyTipManager.notify('没有可删除的记录。');
	}
}

function $xa_deleteItems(dataGrid, updateAction, callBack) {
	var selection = dataGrid.get("selection");
	if (selection.length == 0) {
		var list = dataGrid.get("dataSet").getData(dataGrid.get("dataPath"));
		if (list && list.current) {
			dorado.MessageBox.confirm("确认要删除当前记录么？", {
				icon : "WARNING",
				title : "删除记录",
				callback : function() {
					list.current.remove();
					updateAction.execute(callBack);
				}
			});
		} else {
			dorado.widget.NotifyTipManager.notify("请选择要删除的记录!");
		}
	} else {
		dorado.MessageBox.confirm("确认要删除选中的记录么？", {
			icon : "WARNING",
			title : "删除记录",
			callback : function() {
				selection.each(function(item) {
					item.remove();
				});
				updateAction.execute(callBack);
			}
		});
	}
}

function $xa_dialogSaveItem(dataSet, dataPath, updateAction, dialog, callback) {
	var entity = dataSet.getData(dataPath);
	if (updateAction.get("hasUpdateData")) {
		updateAction.execute(function() {
			dialog && dialog.hide();
			if (callback instanceof Function) {
				callback();
			}
		});
	} else {
		dialog && dialog.hide();
		if (callback instanceof Function) {
			callback();
		}
	}
}

function $xa_dialogCancelItem(dataSet, dataPath, dialog) {
	var entity = dataSet.getData(dataPath);
	if (entity && entity.isDirty()) {
		dorado.MessageBox.confirm("确认放弃当前修改？", {
			title : "关闭编辑窗口",
			callback : function() {
				entity.cancel();
				dialog && dialog.hide();
			}
		});
	} else {
		dialog && dialog.hide();
	}
}

function $xa_queryItem(dataSet, autoformQuery, dataPath) {
	dataSet.set("parameter", autoformQuery.get("entity").toJSON()).flushAsync();
}

function $xa_queryReferenceItem(entity, reference, autoformQuery) {
	var queryJson = autoformQuery.get("entity").toJSON();
	var parameter = reference.get("parameter");
	var lastQueryJson = autoformQuery.lastQueryJson;
	if (lastQueryJson) {
		for ( var p in lastQueryJson) {
			parameter.remove(p);
		}
	}
	parameter.put(queryJson);
	entity.reset(reference.get("name"));
	autoformQuery.lastQueryJson = queryJson;
}

function $xa_resetQueryform(dataSetQuery, config) {
	if (!config) {
		config = {};
	}
	dataSetQuery.set("data", config);
}