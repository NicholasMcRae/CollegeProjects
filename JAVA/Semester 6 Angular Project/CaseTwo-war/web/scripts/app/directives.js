(function(app) {
   app.directive('confirmClick', function(){
       return {
           restrict: 'A',
           priority: 2,           
           link: function(scope, element, attr) {
               var msg = attr.confirmationNeeded || "Really Delete?";
               var clickAction = attr.ngClick;
               element.bind('click', function() {
                   if(window.confirm(msg)){
                       scope.$apply(clickAction);
                   }
               });
       }
   }; 
});
}(angular.module('case1')));

(function(app) {
   app.directive('myHelloWorld', function(){
       return {
           restrict: 'E',
           replace: 'true',
           template: '<h2>HelloWorld!!</h2>'
   }; 
});
   
   app.directive("myOtherHelloWorld", function() {  
        return {
            restrict: 'E',
            replace: 'true',
            scope: { sz: '@'},
            template: '<div style="font-size:{{sz}}px;">Hello OtherWorld!! ' +
                    'at {{sz}} pixels</div>'
        };
   });
   
   app.directive("myThirdDirective", function() {  
        return {
            restrict: 'E',
            replace: 'true',
            scope: { customer: '='},
            template: '<div>Ouput from the 3rd example<br/>' +
                    '<input ng-model="customer.name" required type="text" /></div>'
        };
   });
   
   app.directive("myFourthDirective", function() {  
    function link($scope, element, attrs) {
            element.bind('input', function(){
                $scope.myCustomer.combined = $scope.myCustomer.name + ' '
                    + attrs.lastname;
            
                if($scope.myCustomer.name === 'Nick'){
                    alert($scope.myCustomer.combined + ' lives on ' + $scope.myCustomer.street);
                    element.css('visibility', 'hidden');
                }
            });
       }
       
       return {
            restrict: 'E',
            replace: 'true',
            template: '<div>Enter "John" to complete 4th directive<br/>' +
                    '<input ng-model="myCustomer.name" required type="text"/></div>',
            link: link
        };
   });
   
   app.directive("myReqfld", function() {  
    function link($scope, element, attrs) {
            var origVal = $scope.ngModel;
            element.bind('input', function(){
                if(origVal !== $scope.ngModel){
                    element.addClass('changed');
                }
                else{
                    element.removeClass('changed');
                }
            });
       }
       
       return {
            restrict: 'E',
            replace: 'true',
            scope: {ngModel: '=', size: '@', label: '@', max: '@'},
            template:'<div>' + '<div class="col-xs-5 col-lg-6 text-right">{{label}}:</div>' +
                    '<div class="col-xs-7 col-lg-6"><input ng-model="ngModel" ' +
                    'required type="text" maxlength={{max}} size={{size}}></div>' +
                    '<div ng-show="!ngModel" class="col-xs-11 custom-error pull-right">' +
                    '{{label}} is required!</div></div>',
            link: link
        };
   });
   
  app.directive("myReqMsrp", function() {  
    function link($scope, element, attrs) {
            element.bind('input', function(){ 
                var origVal = $scope.ngModel;
                
                if(isNumeric(origVal)){
                    element.addClass('changed');
                    $scope.msrp = false;
                }
                else{
                    element.removeClass('changed');
                    $scope.msrp = true;
                }
            });
       }
       
       return {
            restrict: 'E',
            replace: 'true',
            scope: {ngModel: '=', size: '@', label: '@', max: '@', msrp: '='},
            template:'<div>' + '<div class="col-xs-5 col-lg-6 text-right">{{label}}:</div>' +
                    '<div class="col-xs-7 col-lg-6"><input ng-model="ngModel" ' +
                    'required type="text" maxlength={{max}} size={{size}}></div>' +
                    '<div ng-show="msrp" class="col-xs-11 custom-error pull-right">' +
                    '{{label}} must be a number!</div></div>',
            link: link
        };
   });
   
   app.directive("myReqQoo", function() {  
    function link($scope, element, attrs) {
            element.bind('input', function(){ 
                var origVal = $scope.ngModel;
                
                if(isNumeric(origVal)){
                    element.addClass('changed');
                    $scope.qoo = false;
                }
                else{
                    element.removeClass('changed');
                    $scope.qoo = true;
                }
            });
       }
       
       return {
            restrict: 'E',
            replace: 'true',
            scope: {ngModel: '=', size: '@', label: '@', max: '@', qoo: '='},
            template:'<div>' + '<div class="col-xs-5 col-lg-6 text-right">{{label}}:</div>' +
                    '<div class="col-xs-7 col-lg-6"><input ng-model="ngModel" ' +
                    'required type="text" maxlength={{max}} size={{size}}></div>' +
                    '<div ng-show="qoo" class="col-xs-11 custom-error pull-right">' +
                    '{{label}} must be a number!</div></div>',
            link: link
        };
   });
   
   app.directive("myReqDouble", function() {  
    function link($scope, element, attrs) {
            element.bind('input', function(){ 
                var origVal = $scope.ngModel;
                
                if(isNumeric(origVal)){
                    element.addClass('changed');
                    $scope.flag = false;
                }
                else{
                    element.removeClass('changed');
                    $scope.flag = true;
                }
            });
       }
       
       return {
            restrict: 'E',
            replace: 'true',
            scope: {ngModel: '=', size: '@', label: '@', max: '@', flag: '='},
            template:'<div>' + '<div class="col-xs-5 col-lg-6 text-right">{{label}}:</div>' +
                    '<div class="col-xs-7 col-lg-6"><input ng-model="ngModel" ' +
                    'required type="text" maxlength={{max}} size={{size}}></div>' +
                    '<div ng-show="flag" class="col-xs-11 custom-error pull-right">' +
                    '{{label}} must be a number!</div></div>',
            link: link
        };
   });
   
   app.directive("myReqQoh", function() {  
    function link($scope, element, attrs) {
            element.bind('input', function(){ 
                var origVal = $scope.ngModel;
                
                if(isNumeric(origVal)){
                    element.addClass('changed');
                    $scope.qoh = false;
                }
                else{
                    element.removeClass('changed');
                    $scope.qoh = true;
                }
            });
       }
       
       return {
            restrict: 'E',
            replace: 'true',
            scope: {ngModel: '=', size: '@', label: '@', max: '@', qoh: '='},
            template:'<div>' + '<div class="col-xs-5 col-lg-6 text-right">{{label}}:</div>' +
                    '<div class="col-xs-7 col-lg-6"><input ng-model="ngModel" ' +
                    'required type="text" maxlength={{max}} size={{size}}></div>' +
                    '<div ng-show="qoh" class="col-xs-11 custom-error pull-right">' +
                    '{{label}} must be a number!</div></div>',
            link: link
        };
   });
   
   app.directive("myReqRop", function() {  
    function link($scope, element, attrs) {
            element.bind('input', function(){ 
                var origVal = $scope.ngModel;
                
                if(isNumeric(origVal)){
                    element.addClass('changed');
                    $scope.rop = false;
                }
                else{
                    element.removeClass('changed');
                    $scope.rop = true;
                }
            });
       }
       
       return {
            restrict: 'E',
            replace: 'true',
            scope: {ngModel: '=', size: '@', label: '@', max: '@', rop: '='},
            template:'<div>' + '<div class="col-xs-5 col-lg-6 text-right">{{label}}:</div>' +
                    '<div class="col-xs-7 col-lg-6"><input ng-model="ngModel" ' +
                    'required type="text" maxlength={{max}} size={{size}}></div>' +
                    '<div ng-show="rop" class="col-xs-11 custom-error pull-right">' +
                    '{{label}} must be a number!</div></div>',
            link: link
        };
   });
   
   app.directive("myReqEoq", function() {  
    function link($scope, element, attrs) {
            element.bind('input', function(){ 
                var origVal = $scope.ngModel;
                
                if(isNumeric(origVal)){
                    element.addClass('changed');
                    $scope.eoq = false;
                }
                else{
                    element.removeClass('changed');
                    $scope.eoq = true;
                }
            });
       }
       
       return {
            restrict: 'E',
            replace: 'true',
            scope: {ngModel: '=', size: '@', label: '@', max: '@', eoq: '='},
            template:'<div>' + '<div class="col-xs-5 col-lg-6 text-right">{{label}}:</div>' +
                    '<div class="col-xs-7 col-lg-6"><input ng-model="ngModel" ' +
                    'required type="text" maxlength={{max}} size={{size}}></div>' +
                    '<div ng-show="eoq" class="col-xs-11 custom-error pull-right">' +
                    '{{label}} must be a number!</div></div>',
            link: link
        };
   });
   
   function isNumeric( obj ) {
    return (obj - parseFloat( obj ) + 1) >= 0;
}

}(angular.module('case1')));



