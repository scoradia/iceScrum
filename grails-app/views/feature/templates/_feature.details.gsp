<%@ page import="org.icescrum.core.domain.PlanningPokerGame; org.icescrum.core.utils.BundleUtils" %>
%{--
- Copyright (c) 2014 Kagilum.
-
- This file is part of iceScrum.
-
- iceScrum is free software: you can redistribute it and/or modify
- it under the terms of the GNU Affero General Public License as published by
- the Free Software Foundation, either version 3 of the License.
-
- iceScrum is distributed in the hope that it will be useful,
- but WITHOUT ANY WARRANTY; without even the implied warranty of
- MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
- GNU General Public License for more details.
-
- You should have received a copy of the GNU Affero General Public License
- along with iceScrum.  If not, see <http://www.gnu.org/licenses/>.
-
- Authors:
-
- Vincent Barrier (vbarrier@kagilum.com)
- Nicolas Noullet (nnoullet@kagilum.com)
--}%
<script type="text/ng-template" id="feature.details.html">
<div class="panel panel-default">
    <div id="feature-header"
         class="panel-heading"
         ng-controller="featureHeaderCtrl"
         is-fixed="#right"
         is-fixed-offset-top="1"
         is-fixed-offset-width="-2">
        <h3 class="panel-title">
            <span>{{ selected.name }}</span>
            <div class="pull-right">
                <span class="label label-default"
                      tooltip="${message(code: 'is.backlogelement.id')}">{{ selected.uid }}</span>
                <a ng-if="previous"
                   class="btn btn-xs btn-default"
                   role="button"
                   tabindex="0"
                   href="#feature/{{ previous.id }}"><i class="fa fa-caret-left" title="${message(code:'is.ui.backlogelement.toolbar.previous')}"></i></a>
                <a ng-if="next"
                   class="btn btn-xs btn-default"
                   role="button"
                   tabindex="0"
                   href="#feature/{{ next.id }}"><i class="fa fa-caret-right" title="${message(code:'is.ui.backlogelement.toolbar.next')}"></i></a>
            </div>
        </h3>
        <div class="actions">
            <div class="btn-group"
                 tooltip="${message(code: 'todo.is.feature.actions')}"
                 tooltip-append-to-body="true">
                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                    <span class="fa fa-cog"></span> <span class="caret"></span>
                </button>
            </div>
        </div>
    </div>

    <div id="right-feature-container"
         class="right-properties new panel-body">
        <form ng-submit="update(feature)" name='featureForm' show-validation ng-controller="featureEditCtrl">
            <div class="clearfix no-padding">
                <div class="col-md-6 form-group">
                    <label for="feature.name">${message(code:'is.feature.name')}</label>
                    <input required
                           name="feature.name"
                           ng-model="feature.name"
                           ng-readonly="readOnly()"
                           type="text"
                           class="form-control">
                </div>
                <div class="col-md-6 form-group">
                    <label for="feature.value">${message(code:'is.feature.value')}</label>
                    <select style="width:100%"
                            class="form-control"
                            ng-model="feature.value"
                            ng-readonly="readOnly()"
                            ui-select2>
                        <is:options values="${PlanningPokerGame.getInteger(PlanningPokerGame.INTEGER_SUITE)}" />
                    </select>
                </div>
            </div>
            <div class="clearfix no-padding">
                <div class="col-md-6 form-group">
                    <label for="feature.type">${message(code:'is.feature.type')}</label>
                    <select style="width:100%"
                            class="form-control"
                            ng-model="feature.type"
                            ng-readonly="readOnly()"
                            ui-select2>
                        <is:options values="${is.internationalizeValues(map: BundleUtils.featureTypes)}" />
                    </select>
                </div>
                <div class="col-md-6 form-group">
                    <label for="feature.color">${message(code:'is.feature.color')}</label>
                    <select style="width:100%"
                            class="form-control"
                            ng-model="feature.color"
                            ng-readonly="readOnly()"
                            ui-select2>
                        <is:options values="${is.internationalizeValues(map: BundleUtils.colorsSelect)}" />
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label for="feature.description">${message(code:'is.backlogelement.description')}</label>
                <textarea class="form-control"
                          placeholder="${message(code:'is.ui.backlogelement.nodescription')}"
                          ng-model="feature.description"
                          ng-readonly="readOnly()"/></textarea>
            </div>
            <div class="form-group">
                <input type="hidden"
                       style="width:100%"
                       class="form-control"
                       value="{{ feature.tags.join(',') }}"
                       ng-model="feature.tags"
                       ng-readonly="readOnly()"
                       data-placeholder="${message(code:'is.ui.backlogelement.notags')}"
                       ui-select2="selectTagsOptions"/>
            </div>
            <div class="form-group">
                <label for="feature.notes">${message(code:'is.backlogelement.notes')}</label>
                <textarea is-markitup
                          class="form-control"
                          ng-model="feature.notes"
                          is-model-html="feature.notes_html"
                          ng-show="showNotesTextarea"
                          ng-blur="showNotesTextarea = false"
                          ng-readonly="readOnly()"
                          placeholder="${message(code: 'is.ui.backlogelement.nonotes')}"></textarea>
                <div class="markitup-preview"
                     ng-show="!showNotesTextarea"
                     ng-click="showNotesTextarea = true"
                     ng-focus="showNotesTextarea = true"
                     tabindex="0"
                     ng-bind-html="feature.notes_html | sanitize"></div>
            </div>
        </form>
        <tabset type="{{ tabsType }}">
            <tab heading="${message(code: 'is.ui.backlogelement.attachment')}"
                 active="tabActive['attachments']"
                 scroll-to-tab="#right">
            </tab>
            <tab select="stories(selected)"
                 heading="${message(code: 'todo.is.ui.stories')}"
                 active="tabActive['stories']"
                 scroll-to-tab="#right">
                <table class="table table-striped">
                    <tbody ng-include src="'nested.stories.html'"></tbody>
                </table>
            </tab>
        </tabset>
    </div>
</div>
</script>