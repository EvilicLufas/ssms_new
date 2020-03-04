/**
 * 需要用到的js：
 * 		jquery.js
 * 		json2.js
 * 		checkutil.js
 * 示例：
 *  	var dataObj01 = [{
 *  		name: "spMenu1",
 *  		iconClasses: "fa fa-chrome",
 *  		subMenus: [{
 *  			name: "subMenu11",
 *  			url: "#"
 *  		}, {
 *  			name: "subMenu12",
 *  			url: "#"
 *  		}, {
 *  			name: "subMenu13",
 *  			url: "#"
 *  		}, {
 *  			name: "subMenu14",
 *  			url: "#"
 *  		}]
 *  	}, {
 *  		name: "spMenu2",
 *  		iconClasses: "fa fa-firefox",
 *  		subMenus: [{
 *  			name: "subMenu21",
 *  			url: "#"
 *  		}, {
 *  			name: "subMenu22",
 *  			url: "#"
 *  		}, {
 *  			name: "subMenu23",
 *  			url: "#"
 *  		}, {
 *  			name: "subMenu34",
 *  			url: "#"
 *  		}]
 *  	}, {
 *  		name: "spMenu3",
 *  		iconClasses: "fa fa-internet-explorer",
 *  		subMenus: [{
 *  			name: "subMenu31",
 *  			url: "#"
 *  		}, {
 *  			name: "subMenu32",
 *  			url: "#"
 *  		}, {
 *  			name: "subMenu33",
 *  			url: "#"
 *  		}, {
 *  			name: "subMenu34",
 *  			url: "#"
 *  		}]
 *  	}];
 *  
 *  	var config01 = {
 *  		menuBoxId: "#menuBox01",
 *  		multiple: true,
 *  		openIndex: []
 *  	}
 *  	menuBox.init(config01, dataObj01);
 *  
 *  	var dataObj02 = JSON.stringify(dataObj01);
 *  
 *  	var config02 = {
 *  		menuBoxId: "#menuBox02",
 *  		multiple: false,
 *  		openIndex: []
 *  	}
 *  	menuBox.init(config02, dataObj02);
 *  
 *  	var dataObj03 = {
 *  		name: "spMenu1",
 *  		iconClasses: "fa fa-chrome",
 *  		subMenus: [{
 *  			name: "subMenu11",
 *  			url: "#"
 *  		}, {
 *  			name: "subMenu12",
 *  			url: "#"
 *  		}, {
 *  			name: "subMenu13",
 *  			url: "#"
 *  		}, {
 *  			name: "subMenu14",
 *  			url: "#"
 *  		}]
 *  	};
 *  	var config03 = {
 *  		menuBoxId: "#menuBox03",
 *  		multiple: false,
 *  		openIndex: []
 *  	}
 *  	menuBox.init(config03, dataObj03);
 *  
 *  	var dataObj04 = JSON.stringify(dataObj03);
 *  	var config04 = {
 *  		menuBoxId: "#menuBox04",
 *  		multiple: true,
 *  		openIndex: []
 *  	}
 *  	menuBox.init(config04, dataObj04);
 *  
 * @author DarkRanger
 * http: //www.cnblogs.com/wrcold520/
 */

! function($, JSON, checkutil) {
	"use strict";

	var menuBox = function() {};
	//要配置的menuBox的菜单id
	menuBox.menuBoxId = undefined;
	//是否可以显示多个上级菜单的子菜单
	menuBox.multiple = false;
	//默认关闭所有一级菜单
	menuBox.openIndex = [];

	//menuBox初始化方法
	menuBox.init = function(config, data) {

		var cntMenuBox = new menuBox();

		var menuBoxEle;
		//定义上级菜单spMenu数组
		var spMenus;

		var boxId = config.menuBoxId;
		var menuBoxEle = $(boxId);

		if(boxId == undefined) {
			console.warn("Your menuBox config has not key['menuBoxId'], configure failed!!!\nPlease configure your unique menuBox by MenuBoxId!");
			return;
		} else if(menuBoxEle.length == 0) {
			console.warn("Cannot find your menuBox[id: " + boxId + "], configure failed!!! ");
			return;
		} else {
			cntMenuBox.menuBoxId = $(config.menuBoxId) ? config.menuBoxId : undefined;
			menuBoxEle = $(cntMenuBox.menuBoxId);
			if(data) {
				genDomByData(menuBoxEle, data);
			}
		}

		if(config.multiple == undefined) {
			console.warn("Your config has not key['multiple'], default value is false that means you could open one spMenu at most at the same time!");
		} else {
			cntMenuBox.multiple = config.multiple;
		}

		if(config.openIndex == undefined) {
			console.warn("your config has not key['openIndex'] , default value is a Number Array which's length is 0!");
		} else if(!config.openIndex instanceof Array) {
			throw new Error("your config 'openIndex' should be a number Array");
		} else {
			cntMenuBox.openIndex = unique(config.openIndex, false);
		}

		//确定对应的menuBox
		cntMenuBox.menuBoxId = config.menuBoxId;
		//是否打开其他某一个的时候关闭其他选项
		var closeOthers = !cntMenuBox.multiple;
		//初始化点击事件
		initClickEvent(cntMenuBox, closeOthers);
		//确定上级菜单数组
		spMenus = $(cntMenuBox.menuBoxId + " .spMenu");
		//打开传入的数组
		for(var i in cntMenuBox.openIndex) {
			var index = cntMenuBox.openIndex[i];
			var spMenu = spMenus[index];
			if(spMenu) {
				openSpMenu(cntMenuBox.menuBoxId, index);
				if(!cntMenuBox.multiple) {
					break;
				}
			}
		}
	};
	/**
	 * 循环创建dom树
	 * @param {Object} menuBoxEle menuBoxEle的根元素（Jquery对象元素）
	 * @param {Object} data
	 */
	function genDomByData(menuBoxEle, data) {
		var dataObj;
		//JS Object or JS Array
		if(checkutil.isArray(data)) {
			dataObj = data;
		} else if(checkutil.isObject(data)) {
			dataObj = [data];
		}
		//JSONString
		else if(checkutil.isString(data)) {
			var menuObj;
			try {
				var menuJson = JSON.parse(data);
				if(checkutil.isArray(menuJson)) {
					menuObj = menuJson;
				} else if(checkutil.isObject(menuJson)) {
					menuObj = [menuJson];
				}
				dataObj = menuObj;
			} catch(e) {
				throw new Error("Please modify your data to a standard jsonString or Array!!!\n请修改您传入的data为标准的json字符串或者数组！！！");
			}
		}

		//创建ul
		var spMenuBoxEle = $("<ul>", {
			"class": "spMenuBox",
		});
		//循环创建li
		$.each(dataObj, function(index, spMenuItem) {

			var spMenuItemEle = $("<li>", {
				"class": "spMenuItem"
			});
			//创建li下面的div
			var spMenuDiv = $("<div>", {
				"class": "spMenu",
			});

			//创建div下面文本前面的icon
			var iconBefore = $("<i>", {
				"class": spMenuItem.iconClasses,
			});
			//创建文本
			var menuSpan = $("<span>", {
				text: spMenuItem.name,
			});
			//创建div下面文本后面的icon
			var iconAfter = $("<i>", {
				"class": "fa fa-2x fa-angle-down"
			});

			iconBefore.appendTo(spMenuDiv);
			menuSpan.appendTo(spMenuDiv);
			iconAfter.appendTo(spMenuDiv);

			spMenuDiv.appendTo(spMenuItemEle);
			//创建子menu的ul
			var subMenuBox = $("<ul>", {
				"class": "subMenuBox",
			})
			$.each(spMenuItem.subMenus, function(index, subMenu) {
				//<li><span class="subMenu">菜单1.1</span></li>
				var subMenuItem = $("<li>", {})
				var subMenuSpan = $("<a>", {
					"class": "subMenu",
					"href": subMenu.url,
					text: subMenu.name
				})
				subMenuSpan.appendTo(subMenuItem);
				subMenuItem.appendTo(subMenuBox);
			});
			subMenuBox.appendTo(spMenuItemEle);

			spMenuItemEle.appendTo(spMenuBoxEle);
		});
		spMenuBoxEle.appendTo(menuBoxEle);
	}

	function unique(arr) {
		var result = [],
			hash = {};
		for(var i = 0, elem;
			(elem = arr[i]) != null; i++) {
			if(!hash[elem]) {
				result.push(elem);
				hash[elem] = true;
			}
		}
		return result;
	}

	//初始化点击事件
	function initClickEvent(menuBox, closeOthers) {
		$(menuBox.menuBoxId + " .spMenu").on("click", function() {
			var cntSpMenu$ = $(this);
			//要切换的元素
			cntSpMenu$.next().slideToggle();
			cntSpMenu$.parent().toggleClass("active")

			var cntSpMenu = cntSpMenu$['context'];
			if(closeOthers == true) {
				var spMenus = $(menuBox.menuBoxId + " .spMenu");
				$.each(spMenus, function(index, spMenu) {
					if(cntSpMenu != spMenu) {
						closeSpMenu(menuBox.menuBoxId, index);
					}
				});
			}
		});
	}

	//打开某一个item
	function openSpMenu(menuBoxId, index) {
		//要切换的元素
		var spItem = $(menuBoxId + " .spMenu:eq(" + index + ")");
		spItem.next().slideDown();
		spItem.parent().addClass("active")
	}
	//关闭某一个item
	function closeSpMenu(menuBoxId, index) {
		//要切换的元素
		var spItem = $(menuBoxId + " .spMenu:eq(" + index + ")");
		spItem.next().slideUp();
		spItem.parent().removeClass("active")
	}
	//切换某一个item
	function toggleSpMenu(menuBoxId, index) {
		//要切换的元素
		var spItem = $(menuBoxId + " .spMenu:eq(" + index + ")");
		spItem.next().slideToggle();
		spItem.parent().toggleClass("active")
	}

	window.menuBox = menuBox;
}($, JSON, checkutil);