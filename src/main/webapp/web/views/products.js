/*******************************************************************************
 * Controller to handle interfacing with the RESTful endpoint
 */
$.ajaxSetup({
	cache : false
});

var utils = {
	_url : '',
	setup : function(u) {
		this._url = u;
	},
	url : function(u) {
		return this._url + u;
	},
	get : function(url, data, cb) {
		$.ajax({
			type : 'GET',
			url : url,
			cache : false,
			dataType : 'json',
			contentType : 'application/json; charset=utf-8',
			success : cb,
			error : function() {
				alert('error trying to retrieve ' + u);
			}
		});
	},
	put : function(url, data, cb) {
		var k = '_method', v = 'PUT';
		data[k] = v;
		var headers = {};
		headers[k] = v;
		$.ajax({
			type : 'POST',
			url : url,
			cache : false,
			headers : headers,
			data : data,
			success : function(result) {
				cb(result);
			},
			error : function(e) {
				console.log('error PUT\'ing to url ' + url + '. '
						+ JSON.stringify(e));
			}
		}); // todo

	},
	post : function(u, data, cb) {
		$.ajax({
			type : 'POST',
			url : u,
			cache : false,
			dataType : 'json',
			data : data,
			contentType : 'application/json; charset=utf-8',
			success : cb,
			error : function() {
				alert('error trying to post to ' + u);
			}
		});
	}
};

function ProductCtrl($scope) {
	$scope.products = [];
	$scope.listTypeOfSales = [];

	var u = utils.url('/deviscalculator/prm/getListTypeOfSales');
	utils.get(u, {}, function(listTypeOfSales) {
		$scope.$apply(function() {
			$scope.listTypeOfSales = listTypeOfSales;
		});
	});

	$scope.query = '';

	$scope.searchResultsFound = function() {
		return $scope.products != null && $scope.products.length > 0;
	};

	$scope.load = function(product) {
		$scope.product = product;
		$scope.id = product.id;
	};

	$scope.search = function() {
		var u = utils.url('/prd/search?q=' + $scope.query);
		utils.get(u, {}, function(products) {
			$scope.$apply(function() {
				$scope.products = products;
				if ($scope.searchResultsFound()) {
					if (products.length == 1) {
						$scope.load(products[0]);
					}
				}
			});
		});
	};
	$scope.isProductLoaded = function() {
		return $scope.product != null && $scope.product.id != null
				&& $scope.product.id > 0;
	};

	function loadProductById(id, cb) {
		var u = utils.url('/prd/products/' + id);
		utils.get(u, {}, cb);
	}

	$scope.lookupProduct = function() {
		loadProductById($scope.id, function(c) {
			$scope.$apply(function() {
				$scope.load(c);
			});
		});
	};

	$scope.save = function() {
		var id = $scope.id;
		var data = {
			name : $scope.product.name,
			price :  parseFloat($scope.product.price).toFixed(2),
			typeOfSale : $scope.product.typeOfSale,
			promotion : $scope.product.promotion ? $scope.product.promotion : false,
			promotionRate : $scope.product.promotion ? parseFloat($scope.product.promotionRate).toFixed(2) : parseFloat(0).toFixed(2),
			nbProduct : $scope.product.nbProduct ? $scope.product.nbProduct : 0,
			weight : $scope.product.weight ? parseFloat($scope.product.weight).toFixed(2) : parseFloat(0).toFixed(2)
		};

		function exists(o, p, cb) {
			if (o[p] && o[p] != null) {
				cb(p, o[p]);
			}
		}

		exists($scope.product, 'id', function(pName, val) {
			data[pName] = val;
		});
		exists($scope.product, 'updateDate', function(pN, v) {
			data[pN] = v;
		});
		var idReceivingCallback = function(id) {
			console.log('id is ' + id);
			$scope.$apply(function() {
				$scope.id = id;
				$scope.lookupProduct();
			});

		};

		var u = null;
		if (id != null && id > 0) {
			// then we're simply going to update it
			u = utils.url('/prd/products/' + id);
			console.log('JSON to send' + JSON.stringify(data))
			utils.post(u, JSON.stringify(data), idReceivingCallback);
		} else {
			u = utils.url('/prd/products');
			console.log('JSON to send' + JSON.stringify(data))
			utils.put(u, data, idReceivingCallback);
		}

	};

	$scope.trash = function() {
		$scope.id = null;
		$scope.product = null;
	};
}
