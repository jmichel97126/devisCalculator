<%@ page session="false"%>
<!doctype html>
<html ng-app>
<head>

<script
	src="${pageContext.request.contextPath}/web/assets/js/jquery-1.7.2.min.js"></script>
<script
	src="${pageContext.request.contextPath}/web/assets/js/angular-1.0.0rc6.js"></script>
<script src="${pageContext.request.contextPath}/web/views/products.js"></script>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/web/assets/bootstrap/bootstrap.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/web/views/products.css" />

</head>
<body>
	<script language="javascript" type="text/javascript">
	<!--
		$(function() {
			utils.setup('${pageContext.request.contextPath}');
		});
	//-->
	</script>

	<div ng-controller="ProductCtrl">

		<div
			style="z-index: -21; float: left; padding-top: 10px; padding: 10px; min-width: 250px; max-width: 300px;">
			<form class="well form-search" ng-submit="search()">
				<div>
					<input type="text" id="search" class="input-medium search-query"
						ng-model="query" /> <a href="#" class="icon-search"
						ng-click="search()"></a>
				</div>
				<div style="padding-top: 10px;">
					<div ng-show=" !searchResultsFound()">
						<span class="no-records">(no results)</span>
					</div>

					<div ng-show=" searchResultsFound()">

						<table>
							<tr>
								<th>Name</th>
								<th>Price</th>
							</tr>
							<tr ng-repeat="product in products" ng-click="load(product)">
								<td>{{ product.name }}</td>
								<td>{{ product.price }}</td>
							</tr>
						</table>
					</div>
				</div>
			</form>
		</div>


		<form class="form-horizontal" ng-submit="updateProduct">
			<fieldset>
				<legend>
					<span class="product-visible-{{!isProductLoaded()}}"> Create
						New Product </span> <span class="product-visible-{{isProductLoaded()}}">
						Update Product {{product.name}} price {{product.price}} - ref
						{{product.id}} </span>
				</legend>
				<div class="control-group">
					<label class="control-label" for="name">Name:</label>

					<div class="controls">
						<input class="input-xlarge" id="name" type="text"
							ng-model="product.name" placeholder="name" required="required" />

						<p class="help-block">Change the name</p>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label" for="price">Price</label>

					<div class="controls">
						<input class="input-xlarge" id="price" type="number" step="any"
							ng-model="product.price" placeholder="price" required="required" />

						<p class="help-block">Change the price</p>
					</div>
				</div>

				<div class="control-group">
					<label class="control-label" for="promotion">Promotion</label>

					<div class="controls">
						<input id="promotion" type="checkbox" ng-model="product.promotion"
							placeholder="promotion" />

						<p class="help-block">Change the promotion</p>
					</div>

					<div class="product-visible-{{product.promotion}}">
						<label class="control-label" for="promotionRate">Promotion
							Rate</label>

						<div class="controls">
							<input class="input-xlarge" id="promotionRate" type="number"
								ng-model="product.promotionRate" placeholder="PromotionRate" />

							<p class="help-block">Change the promotion rate</p>
						</div>
					</div>
				</div>

				<div class="control-group">
					<label class="control-label" for="typeOfSale">Type of sales</label>

					<div class="controls">
						<select class="form-control" id="typeOfSale"
							ng-model="product.typeOfSale">
							<option ng-repeat="item in listTypeOfSales" value="{{item.code}}">{{item.name}}</option>
						</select>
						<p class="help-block">Change the type</p>
					</div>

					<div ng-show="product.typeOfSale == 'batches'" class="controls">
						<label class="control-label" for="nbProduct">Number of
							products </label> <input class="input-xlarge" id="nbProduct" type="number"
							ng-model="product.nbProduct" placeholder="number of products"
							required="required" />
						<p class="help-block">Change the number of product in batch</p>
					</div>

					<div ng-show="product.typeOfSale == 'weight'" class="controls">
						<label class="control-label" for="weight">Weight of
							products </label> <input class="input-xlarge" id="weight" type="number"
							ng-model="product.weight" placeholder="weight"
							required="required" />
						<p class="help-block">Change the weight to pay</p>
					</div>
					
				</div>

				<div class="form-actions">
					<button type="submit" class="btn btn-primary" ng-click="save()"
						ng-model-instant>
						<a class="icon-plus"></a> Save
					</button>
					<button class="btn " ng-click="trash()">
						<a class="icon-trash"></a> Cancel
					</button>
				</div>
			</fieldset>
		</form>
	</div>
</body>
</html>