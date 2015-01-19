/*
 * File included in starter project
 */

(function(app) {
    
    // AddVendorCtrl - angularjs controller for addVendor.html
    var AddVendorCtrl = function($scope, RESTFactory) {
        
        var baseurl = 'webresources/vendor';
        $scope.vendor = new Object();
        $scope.createVendor = function() {
            $scope.status = "Wait...";
            RESTFactory.restCall('post', baseurl, -1, $scope.vendor).then(function(results) {
                $scope.status = results;
            }, function(error) {
                $scope.status = 'Unable to create vendor: ' + error;
            });
        }; // createVendor
        
    };
    
    app.controller('AddVendorCtrl', ['$scope', 'RESTFactory', AddVendorCtrl]);
})(angular.module('case1'));  