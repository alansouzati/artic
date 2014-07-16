'use strict';

var artic = angular.module('artic', ['ngRoute']);

artic.config(function ($routeProvider) {

    $routeProvider.
        when('/', {
            redirectTo: '/overview'
        }).
        when('/overview', {
            templateUrl: 'views/overview.html'
        }).
        when('/api', {
            templateUrl: 'views/api.html'
        }).
        when('/configuration', {
            templateUrl: 'views/configuration.html'
        }).
        when('/examples', {
            templateUrl: 'views/examples.html'
        });
});