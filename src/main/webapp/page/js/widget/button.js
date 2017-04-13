var jmButton = (function() {
    return {
        name : '按钮',
        type : 'button',
        htmlSnippet : '<button id="${id}" class="${clazz}" type="${type}" data-widget-id="${widgetId}" data-widget-type="jmButton">${text}</button>',
        attributes : [ {
            name : 'id',
            value : 'id',
            description : 'the id of the button'
        }, {
            name : 'type',
            value : 'type',
            description : 'the type of the button'
        }, {
            name : 'text',
            value : 'text',
            description : 'the text of the button'
        } ],
        initHtml : function(attrs) {
            var defaultAttrs = {
                id : '',
                type : 'button',
                text : '按钮',
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

                if (key == 'text') {
                    target.text(attrs[key]);
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
                text : target.text(),
                widgetId : widgetId,
                widgetType : 'jmButton'
            };

            return attr;
        }
    };
})();