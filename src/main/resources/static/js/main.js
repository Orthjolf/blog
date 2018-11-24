function getIndex(list, id) {
    for (var i = 0; i < list.length; i++) {
        if (list[i].id === id) {
            return i;
        }
    }

    return -1;
}


var articleApi = Vue.resource('/article{/id}');

Vue.component('article-form', {
    props: ['articles', 'articleAttr'],
    data: function () {
        return {
            text: '',
            id: ''
        }
    },
    watch: {
        articleAttr: function (newVal, oldVal) {
            this.text = newVal.text;
            this.id = newVal.id;
        }
    },
    template:
    '<div>' +
    '<input type="text" placeholder="Write something" v-model="text" />' +
    '<input type="button" value="Save" @click="save" />' +
    '</div>',
    methods: {
        save: function () {
            var article = {text: this.text};

            if (this.id) {
                articleApi.update({id: this.id}, article).then(result =>
                    result.json().then(data => {
                        var index = getIndex(this.articles, data.id);
                        this.articles.splice(index, 1, data);
                        this.text = ''
                        this.id = ''
                    })
                )
            } else {
                articleApi.save({}, article).then(result =>
                    result.json().then(data => {
                        this.articles.push(data);
                        this.text = ''
                    })
                )
            }
        }
    }
});

Vue.component('article-row', {
    props: ['article', 'editMethod', 'articles'],
    template: '<div class="card">' +
    '              <div class="card-header">' +
    '                  <a class="card-link" data-toggle="collapse">({{ article.id }})</a>' +
    '                  <div class="card-body">' +
    '                      {{ article.text }}' +
    '                  </div>' +
    '              </div>' +
    '              <div class="card-footer">' +
    '              <button type="button" class="btn btn-danger" @click="del">Удалить</button> ' +
    '              <button type="button" class="btn btn-primary" @click="edit">Изменить</button>'+
    '              </div>' +
    '          </div>',
    methods: {
        edit: function () {
            this.editMethod(this.article);
        },
        del: function () {
            articleApi.remove({id: this.article.id}).then(result => {
                if (result.ok) {
                    this.articles.splice(this.articles.indexOf(this.article), 1)
                }
            })
        }
    }
});

Vue.component('articles-list', {
    props: ['articles'],
    data: function () {
        return {
            article: null
        }
    },
    template:
    '<div style="position: relative">' +
    '<article-form :articles="articles" :articleAttr="article" />' +
    '<article-row v-for="article in articles" :key="article.id" :article="article" ' +
    ':editMethod="editMethod" :articles="articles" />' +
    '</div>',
    created: function () {
        articleApi.get().then(result =>
            result.json().then(data =>
                data.forEach(article => this.articles.push(article))
            )
        )
    },
    methods: {
        editMethod: function (article) {
            this.article = article;
        }
    }
});

var app = new Vue({
    el: '#app',
    template: '<articles-list :articles="articles" />',
    data: {
        articles: []
    }
});