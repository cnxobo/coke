(function(win) {
	var viewMap = {};
	var __view;
	var __dataGrid;
	var __dataRebuild;
	var __ignoreProperties;
	var __importCallback;

	function exportData() {
		var dataSet = __dataGrid.get("dataSet");
		var dataPath = __dataGrid.get("dataPath");
		var selections = __dataGrid.get("selection");
		var result = [];
		if (selections.length) {
			selections.each(function(entity) {
				result.push(entity.toJSON());
			})
		} else {
			var list = dataSet.getData(dataPath);
			var currentEntity = list.current;
			if (currentEntity) {
				console.log(currentEntity);
				result.push(currentEntity.toJSON());
			}
		}
		if (__ignoreProperties && jQuery.isFunction(__ignoreProperties.each)) {
			__ignoreProperties.each(function(property) {
				setData(result, property, null);
			});
		}
		var jsonData = dorado.JSON.stringify(result);
		return LZString.compressToBase64(jsonData);
	}

	function setEntityState(entity, state) {
		if (!(entity instanceof dorado.Entity)) {
			return;
		}
		entity.state = state;
		var json = entity.toJSON();
		for(var p in json){
			var value = entity.get(p);
			if (value instanceof dorado.Entity){
				setEntityState(value, state);
			} else if (value instanceof dorado.EntityList){
				value.each(function(item){
					setEntityState(item, state);
				});
			}
		}
	}

	function importData(data) {
		var dataSet = __dataGrid.get("dataSet");
		var dataPath = __dataGrid.get("dataPath");

		var object = dorado.JSON.parse(LZString.decompressFromBase64(data));
		console.log(object);
		if (jQuery.isFunction(object.each)) {
			object.each(function(item) {
				var entity = dataSet.getData(dataPath).insert(item);
				setEntityState(entity, dorado.Entity.STATE_NEW);
			})
		} else {
			dataSet.getData(dataPath).insert(object);
		}
		if (jQuery.isFunction(__importCallback)) {
			__importCallback();
		}
	}

	function setData(data, property, value) {
		var properties = property.split(".");

		if (properties.length > 1) {
			if (jQuery.isFunction(data.each)) {
				data.each(function(item) {
					if (!item[properties[0]]) {
						item[properties[0]] = {};
					}
					setData(item[properties[0]], properties.slice(1).join("."),
							value);
				})
			} else {
				if (!data[properties[0]]) {
					data[properties[0]] = {};
				}
				setData(data[properties[0]], properties.slice(1).join("."),
						value);
			}
		} else {
			if (jQuery.isFunction(data.each)) {
				data.each(function(item) {
					item[property] = value;
				})
			} else {
				data[property] = value;
			}
		}
	}

	win.coke = win.coke || {};

	win.coke.DataTransfer = function(view, dataGrid, ignoreProperties,
			importCallback) {
		__view = view;
		__dataGrid = dataGrid;
		if (typeof ignoreProperties == "string") {
			__ignoreProperties = ignoreProperties.split(",");
		}
		__importCallback = importCallback;
	}
	win.coke.DataTransfer.prototype.exportData = exportData;
	win.coke.DataTransfer.prototype.importData = importData;
	win.coke.DataTransfer.setData = setData;

}(window))