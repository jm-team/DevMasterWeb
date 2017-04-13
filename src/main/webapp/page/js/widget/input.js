var jmInput = (function() {
    var input = {
        name : '输入框',
        type : 'input',
        htmlSnippet : '<input id="${id}" type="${type}" class="${clazz}" name="${name}" value="${value}" data-widget-id="${widgetId}" data-widget-type="jmInput"/>',
        attributes : [ {
            name : 'id',
            value : 'id',
            description : 'the id of the input'
        }, {
            name : 'type',
            value : 'type',
            description : 'the type of the input'
        }, {
            name : 'name',
            value : 'name',
            description : 'the name of the input'
        }, {
            name : 'value',
            value : 'value',
            description : 'the value of the input'
        } ],
        initHtml : function(attrs) {
            var defaultAttrs = {
                id : '',
                type : 'text',
                name : '',
                value : '',
                clazz : 'target',
                widgetId : widgetUtils.randomNumber()
            };

            if (jQuery.isPlainObject(attrs)) {
                for ( var k in attrs) {
                    defaultAttrs[k] = attrs[k];
                }
            }

            var html = this.htmlSnippet;

            for ( var key in defaultAttrs) {
                html = html.replace('${' + key + '}', defaultAttrs[key]);
            }

            return html;
        },
        setAttr : function(attrs) {
            if (!jQuery.isPlainObject(attrs)) {
                console.log(attrs + 'is not a plain object');
                return;
            }

            if (!widgetUtils.isString(attrs['widgetId']) || attrs['widgetId'].length == 0) {
                console.log('attribute object must contains "widgetId" property and "widgetId" property cannot be empty');
                return;
            }

            var target = $('[data-widget-id=' + attrs['widgetId'] + ']');

            for ( var key in attrs) {
                if (key == 'widgetId') {
                    continue;
                }

                var value = attrs[key];

                if (!widgetUtils.isString(value)) {
                    continue;
                }

                if (key == 'value') {
                    target.val(attrs[key]);
                } else {
                    target.attr(key, attrs[key]);
                }
            }
        },
        getAttr : function(widgetId) {
            if (!widgetUtils.isString(widgetId) || widgetId.length == 0) {
                console.log('widgetId cannot be empty');
                return;
            }

            var target = $('[data-widget-id=' + widgetId + ']');
            var attr = {
                id : target.attr('id'),
                type : target.attr('type'),
                name : target.attr('name'),
                value : target.val(),
                widgetId : widgetId,
                widgetType : 'jmInput'
            };

            return attr;
        }
    };

    return input;
})();