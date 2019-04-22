/*
 * Created by: Nick McRae
 * Purpose: Controller for vendor modal
 * Revisions:
 * Sep. 18th - Initial Implementation
 */

(function(app) {
    var ProductsCtrl = function($scope, $modal, RESTFactory) {
        var baseurl = 'webresources/product';
        
        var init = function() {
            
            $scope.status = 'Loading Products...';
            $scope.vendors = RESTFactory.restCall('get', 'webresources/vendor', -1, '').then(function(vendors){                
                if (vendors.length > 0){
                    $scope.vendors = vendors;
                }
                else {
                    $scope.status = 'Produces not retrieved code - ' + vendors;
                }
            }, function(reason){
                $scope.status = 'Products not retrieved ' + reason;
            });
            
            $scope.products = RESTFactory.restCall('get', 'webresources/product', -1, '').then(function(products){                
                if (products.length > 0){
                    $scope.products = products;
                    $scope.status = 'Products loaded!';
                }
                else {
                    $scope.status = 'Products not retrieved code - ' + products;
                }
            }, function(reason){
                $scope.status = 'Products not retrieved ' + reason;
            });
        }; //init
        
        init();
        
        $scope.product = new Object();
        
        
        
        $scope.selectRow = function(row, product){
            if(row < 0){
                $scope.todo = 'add';
                $scope.product = new Object();
            }
            else {
                $scope.todo = 'update';
                $scope.product = product;
                $scope.selectedRow = row;                
            }
            
            var modalInstance = $modal.open({
            templateUrl: 'partials/productsModal.html',
            controller: 'ProductModalCtrl',
            scope: $scope,
            backdrop: 'static'
            });
            
            modalInstance.result.then(function(results){
                
                for(var i = 0; i < $scope.products[i].code; i++)
                {
                   if($scope.products[i].code === results.productCode)
                   {
                       $scope.products.splice(i, 1);
                   }
                }
                
               if (results.numOfRows === 1){
                    $scope.status = results.message;
                }
                else{
                    $scope.status = 'Something went wrong!';
                }
            }, function() {
               $scope.update = 'Product Not Updated!';
           }, function(reason) {
               $scope.update = reason;
            });
        };//selectRow
    };
    
    app.controller('ProductsCtrl', ['$scope', '$modal', 'RESTFactory', ProductsCtrl]);
})(angular.module('case1'));  


