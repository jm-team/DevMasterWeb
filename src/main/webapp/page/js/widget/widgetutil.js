/**
 * 
 */
var widgetUtils = (function() {
    var util = {
        inputTypes : [ 'text', 'password', 'hidden' ]
    };

    util.isString = function(args) {
        return typeof (args) == 'string';
    }

    util.randomNumber = function() {
        return this.randomFromInterval(1, 1e6)
    }
    
    util.randomFromInterval = function(e, t) {
        return Math.floor(Math.random() * (t - e + 1) + e)
    }

    return util;
})();