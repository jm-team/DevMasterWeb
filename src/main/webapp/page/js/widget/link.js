var jmLink = (function() {
    return {
        name : '链接',
        type : 'a',
        htmlSnippet : '<a id="${id}" class="${clazz}" href="${href}" target="${target}" data-widget-id="${widgetId}" data-widget-type="jmLink">${text}</a>',
        attributes : [ {
            name : 'id',
            value : 'id',
            description : 'the id of the link'
        }, {
            name : 'href',
            value : 'href',
            description : 'the href of the link'
        }, {
            name : 'target',
            value : 'target',
            description : 'the target of the link'
        }, {
            name : 'text',
            value : 'text',
            description : 'the text of the link'
        } ],
        initHtml : function(attrs) {
            var defaultAttrs = {
                id : '',
                text : '链接',
                href : '#',
                target : '_self',
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
                href : target.attr('href'),
                target : target.attr('target'),
                text : target.text(),
                widgetId : widgetId,
                widgetType : 'jmLink'
            };

            return attr;
        }
    };
})();