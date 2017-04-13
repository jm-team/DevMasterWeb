var jmLabel = (function() {
    return {
        name : '文本',
        type : 'label',
        htmlSnippet : '<label id="${id}" class="${clazz}" data-widget-id="${widgetId}" data-widget-type="jmLabel">${text}</label>',
        attributes : [ {
            name : 'id',
            value : 'id',
            description : 'the id of the label'
        }, {
            name : 'text',
            value : 'text',
            description : 'the text of the label'
        } ],
        initHtml : function(attrs) {
            var defaultAttrs = {
                id : '',
                text : '文本',
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
                text : target.text(),
                widgetId : widgetId,
                widgetType : 'jmLabel'
            };

            return attr;
        }
    };
})();