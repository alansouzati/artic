'use strict';

var artic = angular.module('artic', ['ngRoute']);

artic.config(function ($routeProvider) {

    $routeProvider.
        when('/', {
            redirectTo: '/overview'
        }).
        when('/overview', {
            controller: 'OverviewCtrl',
            templateUrl: 'views/overview.html'
        });
});