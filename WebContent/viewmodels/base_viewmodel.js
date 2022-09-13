define(
		[ "binder" ],
		function(_binder) {
			var binder = _binder;

			function BaseVM() {
				var self = this;

				ko.validation.init({
					errorElementClass : "has-error",
					errorMessageClass : "help-block",
					decorateInputElement : true
				}, true);

				self.msg = ko.observable();
				self.footerClass = ko.observable("alert alert-info");

				self.onLoad = function() {
				};
				
				self.showItemsPage = function(init) {
					binder.loadView("items",
							"items_viewmodel", "items_div",
							"pageContainer", init);
				};
				
				self.showAttachmentsPage = function(init) {
					binder.loadView("attachments",
							"attachments_viewmodel", "attachments_div",
							"pageContainer", init);
				};
				
				self.showBOMPage = function(init) {
					binder.loadView("bom",
							"bom_viewmodel", "bom_div",
							"pageContainer", init);
				};
				
				self.showAMLPage = function(init) {
					binder.loadView("aml",
							"aml_viewmodel", "aml_div",
							"pageContainer", init);
				};

				self.showInventoryDataPage = function(init) {
					binder.loadView("getInventoryData",
							"getInventoryData_viewmodel", "action_div",
							"pageContainer", init);
				};

				self.showLandingPage = function(init) {
					binder.loadView("criteria_grid", "criteria_grid_viewmodel",
							"criteria_grid_div", null, init);
				};

				self.showCreateCriteriaPage = function(init) {
					binder.loadView("create_criteria",
							"create_criteria_viewmodel", "create_criteria_div",
							null, init);
				};

				self.setFootNote = function(type, msg, timeout) {
					self.msg(msg);
					switch (type) {
					case "error":
						self
								.footerClass("alert alert-danger col-lg-6 col-lg-offset-3 col-md-8 col-md-offset-2");
						break;
					case "success":
						self
								.footerClass("alert alert-success col-lg-6 col-lg-offset-3 col-md-8 col-md-offset-2");
						break;
					case "warning":
						self
								.footerClass("alert alert-warning col-lg-6 col-lg-offset-3 col-md-8 col-md-offset-2");
						break;
					default:
						self
								.footerClass("alert alert-info col-lg-6 col-lg-offset-3 col-md-8 col-md-offset-2");
					}

					$("#index-errorDiv").fadeIn();

					if (timeout)
						setTimeout(function() {
							$("#index-errorDiv").fadeOut();
						}, timeout);
				};

				self.hideMessage = function() {
					$("#index-errorDiv").slideUp("slow");
				};
			}

			return {
				getInstance : function() {
					return new BaseVM();
				}
			};
		});
