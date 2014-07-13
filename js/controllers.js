'use strict';

artic.controller('OverviewCtrl',
    function OverviewCtrl($scope) {
    });


artic.controller('PageNavigationCtrl',
    function PageNavigationCtrl($scope) {

        $scope.pageTabs = [{
            index: 0,
            id: 'overview',
            title: 'Overview',
            icon: 'icon-home'
        }, {
            index: 1,
            id: 'api',
            title: 'API',
            icon: 'icon-table'
        }, {
            index: 2,
            id: 'getting-started',
            title: 'Getting Started',
            icon: 'icon-unlock'
        }, {
            index: 3,
            id: 'examples',
            title: 'Examples',
            icon: 'icon-tasks'
        }];

        $scope.switchPage = function(indx){
            $scope.selectedPage = $scope.pageTabs[indx];
        };

        $scope.selectedPage = $scope.pageTabs[0];
    });

