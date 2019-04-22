/* routes.js
 * Used to setup routes for partial pages and match the page with 
 * the correct controller
 */
(function(app) {
    app.config(['$routeProvider', function($routeProvider) {
            $routeProvider
                    .when('/', {
                        controller: '',
                        templateUrl: 'partials/home.html'
                    })
                    .when('/addVendor', {
                        controller: 'AddVendorCtrl',
                        templateUrl: 'partials/addVendor.html'
                    })
                    .when('/vendors', { 
                        controller: 'VendorsCtrl',
                        templateUrl: 'partials/vendors.html'
                    })
                    .when('/vendorModal', { 
                        controller: 'VendorModalCtrl',
                        templateUrl: 'partials/vendorModal.html'
                    })
                    .when('/products', { 
                        controller: 'ProductsCtrl',
                        templateUrl: 'partials/products.html'
                    })
                    .when('/productsModal', { 
                        controller: 'ProductModalCtrl',
                        templateUrl: 'partials/productsModal.html'
                    })
                    .when('/generator', { 
                        controller: 'generatorCtrl',
                        templateUrl: 'partials/generator.html'
                    })
                    .when('/viewer', { 
                        controller: 'ViewerCtrl',
                        templateUrl: 'partials/viewer.html'
                    })
                    .otherwise({redirectTo: '/'});
        }]);
})(angular.module('case1'));