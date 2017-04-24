jmPagination = (function() {
    var template = '<ul id="${id}" class="${clazz}" data-widget-id="${widgetId}" data-widget-type="jmPagination">' +
                        '<li><a href="#">&laquo;</a></li>' +
                        '<li class="active"><a href="#">1</a></li>' +
                        '<li><a href="#">2</a></li>' +
                        '<li><a href="#">3</a></li>' +
                        '<li><a href="#">4</a></li>' +
                        '<li><a href="#">5</a></li>' +
                        '<li><a href="#">&raquo;</a></li>' +
                    '</ul>';
    


            return {
        name : '分页',
        type : 'pagination',
        htmlSnippet : template,
        attributes : [ {
            name : 'id',
            value : 'id',
            description : 'the id of the pagination'
        }, {
            name : 'totalPage',
            value : 'totalPage',
            description : 'the totalPage of the pagination'
        }, {
            name : 'pageIndex',
            value : 'pageIndex',
            description : 'the pageIndex of the pagination'
        }, {
            name : '上一页',
            value : 'previous',
            description : 'the previous of the pagination'
        }, {
            name : '下一页',
            value : 'next',
            description : 'the next of the pagination'
        } ],
        initHtml : function(attrs) {
            var defaultAttrs = {
                id : '',
                clazz : 'target pagination',
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
            var lis = target.children('li');

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
                } else if (key === 'totalPage') {
                    var totalPage = parseInt(value);

                    if (isNaN(totalPage)) {
                        console.log('totalPage must be a int number');
                        continue;
                    }

                    if (lis.length == totalPage + 2) {
                        continue;
                    }

                    $.each(lis, function(i, li) {
                        if (i > 0 && i < lis.length - 1) {
                            li.remove();
                        }
                    });

                    var next = target.children('li').eq(1);

                    for (var p = 1; p <= totalPage; p++) {
                        next.before('<li><a href="#">' + p + '</a></li>');
                    }
                    
                    target.children('li').eq(1).addClass('active');

                } else if (key === 'pageIndex') {
                    var pageIndex = parseInt(value);

                    if (isNaN(pageIndex)) {
                        console.log('pageIndex must be a int number');
                        continue;
                    }

                    if (pageIndex < 1 || pageIndex > lis.length - 2) {
                        console.log('pageIndex out of index');
                        continue;
                    }

                    lis.removeClass('active');
                    lis.eq(pageIndex).addClass('active');
                } else if (key === 'previous') {
                    if (value.length == 0) {
                        console.log('previous cannot be empty');
                        continue;
                    }

                    lis.eq(0).children('a').text(value);
                } else if (key === 'next') {
                    if (value.length == 0) {
                        console.log('next cannot be empty');
                        continue;
                    }

                    lis.eq(lis.length - 1).children('a').text(value);
                }
            }
        },
        getAttr : function(widgetId) {
            if (!widgetUtils.isString(widgetId) || widgetId.length == 0) {
                console.log('widgetId cannot be empty');
                return;
            }

            var target = $('[data-widget-id=' + widgetId + ']');
            var lis = target.children('li');
            var pageIndex = 0;

            $.each(lis, function(i, li) {
                if (i > 0 && i < lis.length - 1 && $(li).hasClass('active')) {
                    pageIndex = i;
                }
            });

            var attr = {
                id : target.attr('id'),
                totalPage : lis.length - 2,
                pageIndex : pageIndex,
                previous : lis.eq(0).text(),
                next : lis.eq(lis.length - 1).text(),
                widgetId : widgetId,
                widgetType : 'jmPagination'
            };

            return attr;
        }
    };
})();