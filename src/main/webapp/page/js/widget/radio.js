jmRadio = (function() {
    return {
        name : '单选框',
        type : 'radio',
        htmlSnippet : '<input type="radio" id="${id}" name="${name}" value="${value}" class="${clazz}" data-widget-id="${widgetId}" data-widget-type="jmRadio" />',
        attributes : [ {
            name : 'id',
            value : 'id',
            description : 'the id of the radio'
        }, {
            name : 'name',
            value : 'name',
            description : 'the name of the radio'
        }, {
            name : 'value',
            value : 'value',
            description : 'the value of the radio'
        }, {
            name : 'checked',
            value : 'checked',
            description : 'the checked of the radio'
        } ],
        initHtml : function(attrs) {
            var defaultAttrs = {
                id : '',
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
                    target.val(value);
                } else if (key == 'checked') {
                    if (value.toLowerCase() === 'true') {
                        target.attr('checked', 'checked');
                        target.prop('checked', true);
                    } else {
                        target.removeAttr('checked');
                        target.prop('checked', false);
                    }
                } else {
                    target.attr(key, value);
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
                name : target.attr('name'),
                value : target.val(),
                checked : target[0].checked,
                widgetId : widgetId,
                widgetType : 'jmRadio'
            };

            return attr;
        }
    };
})();