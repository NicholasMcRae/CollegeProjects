/*
 * Created by: Nick McRae
 * Purpose: Controller for vendor modal
 * Revisions:
 * Sep. 9th - Initial Implementation
 * Sep. 14th - Add and Delete function
 * Sep. 16th - Commenting and refactoring
 */

(function(app) {
    var VendorsCtrl = function($scope, $modal, RESTFactory) {
        var baseurl = 'webresources/vendor';
        
        var init = function() {
            
            $scope.status = 'Loading Vendors...';
            $scope.vendors = RESTFactory.restCall('get', baseurl, -1, '').then(function(vendors){
                
                if (vendors.length > 0){
                    $scope.vendors = vendors;
                    $scope.status = 'Vendors Retrieved';
                }
                else {
                    $scope.status = 'Vendors not retrieved code - ' + vendors;
                }
            }, function(reason){
                $scope.status = 'Vendors not retrieved ' + reason;
            });
            $scope.vendor = $scope.vendors[0];
        }; //init
        
        $scope.vendor = new Object();
        
        init();
        
        $scope.selectRow = function(row, vendor){
            if(row < 0){
                $scope.todo = 'add';
                $scope.vendor = new Object();
            }
            else {
                $scope.todo = 'update';
                $scope.vendor = vendor;
                $scope.selectedRow = row;                
            }
            
            var modalInstance = $modal.open({
            templateUrl: 'partials/vendorModal.html',
            controller: 'VendorModalCtrl',
            scope: $scope,
            backdrop: 'static'
            });
            
            modalInstance.result.then(function(results){
                
                
                if(results.operation === 'delete')
                {
                    for(var i = 0; i < $scope.vendors[i].length; i++)
                    {
                       if($scope.vendors[i].vendorno === results.vendorno)
                       {
                           $scope.vendors.splice(i, 1);
                       }
                    }
                }                
                
                if (results.numOfRows === 1){
                    $scope.status = results.message;
                }
                else{
                    $scope.status = 'Something went wrong!';
                }
            }, function() {
               $scope.status = results.message;
           }, function(reason) {
               $scope.update = reason;
            });
        };//selectRow
    };
    
    app.controller('VendorsCtrl', ['$scope', '$modal', 'RESTFactory', VendorsCtrl]);
})(angular.module('case1'));  

