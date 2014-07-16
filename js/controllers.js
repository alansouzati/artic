'use strict';
artic.controller('PageNavigationCtrl',
    function PageNavigationCtrl($scope, $location) {

        $scope.pageTabs = [
            {
                index: 0,
                id: 'overview',
                title: 'Overview',
                icon: 'icon-home'
            },
            {
                index: 1,
                id: 'api',
                title: 'API',
                icon: 'icon-table'
            },
            {
                index: 2,
                id: 'configuration',
                title: 'Configuration',
                icon: 'icon-unlock'
            },
            {
                index: 3,
                id: 'examples',
                title: 'Examples',
                icon: 'icon-tasks'
            }
        ];

        $scope.switchPage = function (indx) {
            $scope.selectedPage = $scope.pageTabs[indx];
        };

        $scope.selectedPage = $scope.pageTabs.filter(function (pageTab){
            return $location.path().indexOf(pageTab.id) > -1;
        })[0];

    });

