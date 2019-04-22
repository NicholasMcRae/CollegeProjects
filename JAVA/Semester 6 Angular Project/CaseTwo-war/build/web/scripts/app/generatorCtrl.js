/*
 * Created by: Nick McRae
 * Purpose: Controller for vendor modal
 * Revisions:
 * Sep. 9th - Initial Implementation
 * Sep. 14th - Add and Delete function
 * Sep. 16th - Commenting and refactoring
 */

(function(app) {
    var generatorCtrl = function($scope, RESTFactory) {
        
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
        }; //init
        
        init(); 
        
        var productArray = [];
                       
        $scope.retrieveProducts = function(){
            
            $scope.products = null;
            $scope.addedProducts = [];
            
            $scope.products = RESTFactory.restCall('get', 'webresources/vendor', $scope.vendor.vendorno, '').then(function(products){                
                if (products.length > 0){
                    $scope.products = products;
                    $scope.showGenerator = true;
                    $scope.status = "Products retrieved!"
                    
                    for(var i = 0; i < $scope.products.length ; i++)
                    {
                        productArray.push($scope.products[i]);
                    }
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
        
        var addedProducts = [];
        var subtotal = 0.0;
        var tax = 0.0;
        var total = 0.0;
        $scope.addedProducts = addedProducts;
        $scope.subtotal = subtotal;
        $scope.tax = tax;
        $scope.total = total;
        
        $scope.addItem = function(){
            
            //this is likely still good
             var quantity = document.getElementById("qty");
             var qtySelection = quantity.options[quantity.selectedIndex].text;
             
             //likely still good
             var prodName = document.getElementById("prodID");
             var prodNameSelection = prodName.options[prodName.selectedIndex].text;
             
             //likely still good
             var productAdded = false;
             
             //check to see if the product has already been added
             for(var i = 0; i < $scope.addedProducts.length; i++){
                 if($scope.addedProducts[i].name === prodNameSelection)
                 {
                     productAdded = true;
                     
                     if(qtySelection === '0'){
                         $scope.addedProducts.splice(i, 1);
                         $scope.status = "Item removed!"
                     }
                     else{                         
                         if(qtySelection === "EOQ"){
                             $scope.addedProducts[i].quantity = $scope.addedProducts[i].eoq;
                         }
                         else{
                             $scope.addedProducts[i].quantity = qtySelection;
                         }
                     }
                     
                     $scope.status = 'Item updated!';
                 }
             }
            
            if(!productAdded){
                for(var i = 0; i < productArray.length; i++){
                if(productArray[i].name === $scope.product.name)
                 {
                    if(qtySelection !== '0'){
                        
                        if(qtySelection === "EOQ"){
                             productArray[i].quantity = productArray[i].eoq; 
                             $scope.addedProducts.push(productArray[i]);
                             $scope.status = 'Product added!';
                         }
                         else{
                             productArray[i].quantity = qtySelection;
                             $scope.addedProducts.push(productArray[i]);
                             $scope.status = 'Product added!';
                         }
                        
                    } 
                 }
             }
            }
            
             
            $scope.subtotal = 0.0;
            $scope.tax = 0.0;
            $scope.total = 0.0;
            
            for(var i = 0; i < $scope.addedProducts.length; i++){
                $scope.subtotal = $scope.subtotal + $scope.addedProducts[i].cost * $scope.addedProducts[i].quantity;
                $scope.tax = $scope.tax + (($scope.addedProducts[i].cost * $scope.addedProducts[i].quantity) * 0.13);
             }                          
             $scope.total = $scope.subtotal + $scope.tax;
             
             var x = $scope.subtotal;
             var y = $scope.tax;
             var z = $scope.total;
             
             $scope.finalSubtotal = x.toFixed(2);
             $scope.finalTax = y.toFixed(2);
             $scope.finalTotal = z.toFixed(2);
             
        };//addItem
        
        $scope.selectRow = function(row, product){
            for(var i = 0; i < productArray.length; i++){
                 if(productArray[i].name === product.name)
                 {
                     var quantity = document.getElementById("qty");
                     quantity.value = productArray[i].quantity;
                     var product = document.getElementById("prodID");
                     product.value = productArray[i].name;
                 }
             }
            
        };//selectRow
        
        $scope.showAddPO = true;
        
        $scope.createPurchaseOrder = function() {
            
            var lineItem = {};
            var lineItems = [];
            $scope.showAddPO = false;
            
            for(var i = 0; i < $scope.addedProducts.length; i++){
                var code = $scope.addedProducts[i].code;
                var actualQty = $scope.addedProducts[i].quantity;
                var totalPrice = actualQty * $scope.addedProducts[i].cost;
                lineItem = { productCode : code , quantity : actualQty , price : totalPrice };
                lineItems.push(lineItem);
            }    
            
            $scope.status = "Wait...";
            var PODTO = new Object();
            PODTO.total = $scope.finalTotal;
            PODTO.vendorno = $scope.vendor.vendorno;
            PODTO.items = lineItems;
            
            $scope.PO = RESTFactory.restCall('post', 'webresources/po', $scope.vendor.vendorno, PODTO).then(function(results){
            
                if(results.length > 0){
                    $scope.ponumber = results;
                    $scope.status = 'PO Number: ' + results + ' added!';
                    $scope.notcreated = false;
                    $scope.generatorForm.$setPristine();
                }
                else{
                    $scope.status = 'PO not created - ' + results;
                }
            
            }), function(reason){
                        $scope.status = 'PO not created - ' + reason;
        }};
    
        $scope.displayPDF = function() {
            window.open("http://localhost:8080/Case1/PDFSample?po=" + $scope.ponumber );
        };
    };
    
    app.controller('generatorCtrl', ['$scope', 'RESTFactory', generatorCtrl]);
})(angular.module('case1')); 

