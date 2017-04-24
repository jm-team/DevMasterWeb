jmAlerts = (function() {
    var template = '<div id="${id}" class="${clazz}" data-widget-id="${widgetId}" data-widget-type="jmAlerts">' +
                        '<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>' +
                        '<span>${text}</span>' +
                    '</div>';
    var types = [ 'success', 'info', 'warning', 'danger' ];
    
    return {
        name : '警告',
        type : 'alerts',
        htmlSnippet : template,
        attributes : [ {
            name : 'id',
            value : 'id',
            description : 'the id of the alerts'
        }, {
            name : 'type',
            value : 'type',
            description : 'success | info | warning | danger'
        }, {
            name : 'text',
            value : 'text',
            description : 'the text of the alerts'
        }, {
            name : 'dismissable',
            value : 'dismissable',
            description : 'true | false'
        }, {
            name : 'linkable',
            value : 'linkable',
            description : 'true | false'
        } ],
        initHtml : function(attrs) {
            var defaultAttrs = {
                id : '',
                text : '成功！很好地完成了提交。',
                clazz : 'target alert alert-success alert-dismissable',
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

                if (key === 'id') {
                    target.attr(key, value);
                } else if (key === 'type') {
                    if($.inArray(value, types) == -1){
                        console.log('type is not correct');
                        continue;
                    }
                    
                    target.removeClass('alert-success');
                    target.removeClass('alert-info');
                    target.removeClass('alert-warning');
                    target.removeClass('alert-danger');
                    target.addClass('alert-' + value);
                } else if (key === 'text') {
                    target.children('span').text(value);
                } else if (key === 'dismissable') {
                    if(value === 'true' && !target.hasClass('alert-dismissable')){
                        target.addClass('alert-dismissable');
                        target.prepend('<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>');
                    } else {
                        target.removeClass('alert-dismissable');
                        target.children('button').remove();
                    }
                } else if (key === 'linkable') {
                    if(value === 'true'){
                        target.children('span').html('<a hraf="#">' + target.children('span').text() + '</a>');
                    } else{
                        target.children('span').html(target.children('span').text());
                    }
                }
            }
        },
        getAttr : function(widgetId) {
            if (!widgetUtils.isString(widgetId) || widgetId.length == 0) {
                console.log('widgetId cannot be empty');
                return;
            }

            var target = $('[data-widget-id=' + widgetId + ']');
            var type = '';
            
            if(target.hasClass('alert-success')){
                type = 'success';
            } else if(target.hasClass('alert-info')){
                type = 'info';
            } else if(target.hasClass('alert-warning')){
                type = 'warning';
            } else if(target.hasClass('alert-danger')){
                type = 'danger';
            }   
            
            var attr = {
                id : target.attr('id'),
                type : type,
                text : target.children('span').text(),
                dismissable : target.hasClass('alert-dismissable'),
                linkable : target.children('a').length > 0,
                widgetId : widgetId,
                widgetType : 'jmAlerts'
            };

            return attr;
        }
    };
})();