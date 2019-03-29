//@Controller
//@Bind view.onReady
function managerHrScript() {
	var dataSet = view.id("dataSetHrScript");
	var dialog = view.id("dialogHrScript");
	var dataGrid = view.id("dataGridHrScript");
	var updateAction = view.id("updateActionHrScript");
	var currentPath = "#";
	var listPath = "";

	view.insertHrScript = function() {
		var data = $xa_insertItem(dataSet, listPath, dialog);
		var scriptContent = data.createChild("scriptContent");
		scriptContent.state = dorado.Entity.STATE_NEW;
	};

	view.editHrScript = function() {
		$xa_editItem(dataSet, currentPath, dialog);
	};

	view.deleteHrScript = function() {
		$xa_deleteItems(dataGrid, updateAction);
	};

	view.saveHrScript = function() {
		$xa_dialogSaveItem(dataSet, currentPath, updateAction, dialog);
	};

	view.cancelHrScript = function() {
		$xa_dialogCancelItem(dataSet, currentPath, dialog);
	};
	view.id("buttonAddHrScript").bind("onClick", view.insertHrScript);
	view.id("buttonEditHrScript").bind("onClick", view.editHrScript);
	view.id("buttonDelHrScript").bind("onClick", view.deleteHrScript);
	view.id("buttonSaveHrScript").bind("onClick", view.saveHrScript);
	view.id("buttonCancelHrScript").bind("onClick", view.cancelHrScript);
}

// @Bind #dialogHrScript.beforeClose
function closeDialogHrScript(arg) {
	view.cancelHrScript();
	arg.processDefault = false;
}
