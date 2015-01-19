/*
 * Created by: Nick McRae
 * Purpose: Controller for vendor modal
 * Revisions:
 * Sep. 9th - Initial Implementation
 * Sep. 14th - Add and Delete function
 * Sep. 16th - Commenting and refactoring
 */

(function(app) {
    var VendorModalCtrl = function($scope, $modalInstance, RESTFactory) {
        var baseurl = 'webresources/vendor';
        var retVal = {};
    
        $scope.update = function() {
            RESTFactory.restCall('put', baseurl, -1, $scope.vendor).then(function(results){
                
                if (parseInt(results) !== -1){
                    retVal.numOfRows = parseInt(results);
                    retVal.operation = 'update';
                    retVal.vendorno = $scope.vendor.vendorno;
                    retVal.message = "Vendor " + retVal.vendorno + " updated!";
                    $scope.status = "Vendor " + retVal.vendorno + " updated!";
                }
                else {
                    retVal.numOfRows = -1;
                }  
            }, function(reason) {
                retVal = 'Vendor was not updated! - system error ' + reason;
                $modalInstance.close(retVal);
            });
        };  //update
    
        $scope.add = function() { 
            $scope.status = "Wait...";
            RESTFactory.restCall('post', baseurl, -1, $scope.vendor).then(function(results) {
                $scope.status = "Vendor" + results + " added!";
                retVal.message = results;
                retVal.numOfRows = 1;
                $scope.vendors.add($scope.vendor);
            }, function(error) {
                $scope.status = 'Unable to create vendor: ' + error;
            });
        };  //add
        
        $scope.delete = function() { 
            RESTFactory.restCall('delete', baseurl, $scope.vendor.vendorno, '').then(function(results) {               
                
                if (parseInt(results) !== -1){
                    retVal.numOfRows = parseInt(results);
                    retVal.operation = 'delete';
                    retVal.vendorno = $scope.vendor.vendorno;
                    retVal.message = "Vendor " + retVal.vendorno + " deleted!";
                    $scope.status = "Vendor " + retVal.vendorno + " deleted!";
                }
                else {
                    retVal.numOfRows = -1;
                }
            }, function() {
                retVal.numOfRows = -1;
                $modalInstance.close(retVal);
            });
        };  //delete    
        
        $scope.close = function() { 
            $modalInstance.close(retVal);
        };  //close
    };  //controller
    
app.controller('VendorModalCtrl', ['$scope', '$modalInstance', 'RESTFactory', VendorModalCtrl]);
})(angular.module('case1')); 


