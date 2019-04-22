/*
 * Created by: Nick McRae
 * Purpose: Controller for vendor modal
 * Revisions:
 * Sep. 18th - Initial Implementation
 * Sep. ? - added more flshed out functionality
 */

(function(app) {
    var ProductModalCtrl = function($scope, $modalInstance, RESTFactory) {
        var baseurl = 'webresources/product';
        var retVal = {};
        $scope.flag = true;
        $scope.msrp = true;
        $scope.qoh = true;
        $scope.qoo = true;
        $scope.roq = true;
        $scope.eoq = true; 
        $scope.rop = true;
    
        $scope.update = function() {
            RESTFactory.restCall('put', baseurl, -1, $scope.product).then(function(results){                
                if(parseInt(results) !== -1)    
                {
                    retVal.numOfRows = parseInt(results);
                    retVal.operation = 'updated';
                    retVal.productcode = $scope.product.code;
                    retVal.message = 'Product ' + $scope.product.code + ' is updated!';
                    $scope.status = 'Product ' + $scope.product.code + ' is updated!';
                }
            }, function(reason) {
                retVal = 'Vendor was not updated! - system error ' + reason;
                $modalInstance.close(retVal);
            });
        };  //update
    
        $scope.add = function() { 
            $scope.status = "Wait...";
            RESTFactory.restCall('post', baseurl, -1, $scope.product).then(function(results) {
                $scope.status = results;
                retVal.numOfRows = 1;
                retVal.message = results;
                $scope.products.add($scope.product);
            }, function(error) {
                $scope.status = 'Unable to create product: ' + error;
            });
        };  //add
        
        
        
        $scope.delete = function() { 
            RESTFactory.restCall('delete', baseurl, $scope.product.code, '').then(function(results) {
                
                if (results.substring){
                    retVal.numOfRows = parseInt(results);
                    retVal.operation = 'delete';
                    retVal.productcode = $scope.product.code;
                    retVal.message = "Product " + retVal.productcode + " deleted!";
                }
                else {
                    retVal.numOfRows = -1;
                }
                
                $scope.status = "Product " + retVal.productcode + " deleted!";
            }, function() {
                retVal.numOfRows = -1;
                $modalInstance.close(retVal);
            });
        };  //delete
        
        $scope.close = function() { 
            $modalInstance.close(retVal);
        };  //close        
    };  //controller
    
app.controller('ProductModalCtrl', ['$scope', '$modalInstance', 'RESTFactory', ProductModalCtrl]);
})(angular.module('case1')); 

