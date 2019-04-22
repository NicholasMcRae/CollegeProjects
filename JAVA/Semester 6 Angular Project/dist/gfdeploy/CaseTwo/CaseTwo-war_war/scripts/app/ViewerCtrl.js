/*
 * Created by: Nick McRae
 * Purpose: Controller for viewer
 * Revisions:
 * Sep. 7th - Initial Implementation
 */

(function(app) {
    var ViewerCtrl = function($scope, RESTFactory) {
        
        //init gets the vendors for me
        var init = function() {
            $scope.vendors = RESTFactory.restCall('get', 'webresources/vendor', -1, '').then(function(vendors){                
                if (vendors.length > 0){
                    $scope.vendors = vendors;                    
                    $scope.status = "Vendors retrieved!"
                }
                else {
                    $scope.status = 'Vendors not retrieved code - ' + vendors;
                }
            }, function(reason){
                $scope.status = 'Vendors not retrieved ' + reason;
            });
            
            $scope.showGenerator = false;
            $scope.showProductOrders = false;
            $scope.poitems = [];
        }; //init
        
        init(); 
        
        var productArray = [];
                       
        $scope.retrievePO = function(){            
            $scope.ProductOrders = null;
            
            //need to get ProductOrderArrayList, makes call to webresources/po
            $scope.ProductOrders = RESTFactory.restCall('get', 'webresources/po', $scope.vendor.vendorno, '').then(function(productorders){                
                if (productorders.length > 0){
                    $scope.ProductOrders = productorders;
                    $scope.status = "Product Orders Retrieved!"
                    $scope.showProductOrders = true;
                }
                else if(products.length === 0){
                    $scope.showGenerator = false;
                }
                else {
                    $scope.status = 'Vendors not retrieved code - ' + products;
                }
            }, function(reason){
                $scope.status = 'Vendors not retrieved ' + reason;
            });
        };//retrieveProducts    
        
        $scope.subtotal = 0.0;
        $scope.tax = 0.0;
        $scope.total = 0.0;
        
        $scope.displayPO = function(){ 
            
            var pon = -1;
            var date = new Date();
            
            for(var i = 0; i < $scope.ProductOrders.length; i++)
            {
                if($scope.ProductOrders[i].ponumber === $scope.ponumber)
                {
                    $scope.poitems = $scope.ProductOrders[i].items;
                    pon = $scope.ProductOrders[i].ponumber;
                    date = $scope.ProductOrders[i].date;
                    $scope.showGenerator = true;
                }
            }  
            
            for(var i = 0; i < $scope.poitems.length; i++){
                $scope.subtotal = $scope.subtotal + $scope.poitems[i].cost * $scope.poitems[i].quantity;
                $scope.tax = $scope.tax + (($scope.poitems[i].cost * $scope.poitems[i].quantity) * 0.13);
             }  
             
             $scope.total = $scope.subtotal + $scope.tax;
             
             var x = $scope.subtotal;
             var y = $scope.tax;
             var z = $scope.total;
             
             $scope.finalSubtotal = x.toFixed(2);
             $scope.finalTax = y.toFixed(2);
             $scope.finalTotal = z.toFixed(2);
             
             $scope.status = "PO: " + pon + " Date: " + date.toString();
            
        };//retrieveProducts 
        
        $scope.displayPDF = function() {
            window.open("http://localhost:8080/Case1/PDFSample?po=" + $scope.ponumber );
        };
    };
    
    app.controller('ViewerCtrl', ['$scope', 'RESTFactory', ViewerCtrl]);
})(angular.module('case1')); 


