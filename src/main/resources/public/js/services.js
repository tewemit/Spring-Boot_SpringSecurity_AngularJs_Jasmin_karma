/**
 * Created by tewe on 11/22/2016.
 */
angular.module('services',[])
    .factory('user', function($resource) {
    return $resource('/user/:id'); // Note the full endpoint address
})
    .factory('Page', function() {
    var title = 'HMJ using Spring boot with AngularJS';
    return {
        title: function() { return title; },
        setTitle: function(newTitle) { title = newTitle }
    };
});