import org.icescrum.core.utils.ServicesUtils

/*
* Copyright (c) 2015 Kagilum SAS
*
* This file is part of iceScrum.
*
* iceScrum is free software: you can redistribute it and/or modify
* it under the terms of the GNU Affero General Public License as published by
* the Free Software Foundation, either version 3 of the License.
*
* iceScrum is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU Affero General Public License
* along with iceScrum.  If not, see <http://www.gnu.org/licenses/>.
*
* Authors:
*
* Nicolas Noullet (nnoullet@kagilum.com)
* Vincent BARRIER (vbarrier@kagilum.com)
*
*/

/*
    'windowName' {
        icon                        default: ''                   | String (fontawesome)
        title                       default: ''                   | String (i18n key)
        help                        default: ''                   | String (i18n key)
        secured                     default: "permitAll()"        | String (spEl expression)
        context                     default: "project"            | String (project or ...)
        templatePath                default: "windowName/window"  | String (full path to template)
        menu { => default: null
            defaultPosition         default:null                  | Integer
            defaultVisibility       default:null                  | true/false
        }
        flex =                      default: true                 | true/false
        details =                   default: false                | true/false
        printable =                 default: true                 | true/false
        fullScreen =                default: true                 | true/false
        exportFormats =             default: {[]}                 | Closure return Array of Maps: [code:extension,name:'i18n.code',params:[]]
        before        =             default:null                  | Closure
    }
 */

windows = {
    //Master window : no menu, no context
    'home' {
        context null
    }
    'backlog' {
        details true
        context 'project'
        icon    'inbox'
        help    'is.ui.backlog.help'
        title   'is.ui.backlogs'
        secured 'stakeHolder() or inProject()'
        menu {
            defaultPosition 2
            defaultVisibility true
        }
        embedded = [
                view: 'list',
                viewTypes: ['postits', 'table']
        ]
        exportFormats = {
            [
                    [code: 'pdf', name: message(code: 'is.report.format.postits'), action: 'printPostits', params: [project: params.project]],
                    [code: 'rtf', name: message(code: 'is.report.format.rtf'), params: [project: params.project, format: 'RTF']],
                    [code: 'docx', name: message(code: 'is.report.format.docx'), params: [project: params.project, format: 'DOCX']],
                    [code: 'odt', name: message(code: 'is.report.format.odt'), params: [project: params.project, format: 'ODT']]
            ]
        }
    }
    'feature' {
        details true
        context 'project'
        icon    'puzzle-piece'
        help    'is.ui.feature.help'
        title   'is.ui.feature'
        secured 'isAuthenticated()'
        menu {
            defaultPosition 5
            defaultVisibility true
        }
        embedded = [
                view: 'list',
                viewTypes: ['postits', 'table', 'projectParkingLotChart']
        ]
        exportFormats = {
            [
                    [code: 'rtf', name: message(code: 'is.report.format.rtf'), params: [project: params.project, format: 'RTF']],
                    [code: 'docx', name: message(code: 'is.report.format.docx'), params: [project: params.project, format: 'DOCX']],
                    [code: 'odt', name: message(code: 'is.report.format.odt'), params: [project: params.project, format: 'ODT']]
            ]
        }
    }
    'project' {
        context 'project'
        flex    false
        icon    'dashboard'
        help    'is.ui.project.help'
        title   'is.ui.project'
        menu {
            defaultPosition 1
            defaultVisibility true
        }
        embedded = [
                view: 'projectCumulativeFlowChart',
                viewTypes: ['projectCumulativeFlowChart', 'projectVelocityCapacityChart', 'projectBurnupChart', 'projectBurndownChart', 'projectVelocityChart', 'projectParkingLotChart'],
        ]
    }
    'planning' {
        details true
        context 'project'
        icon    'calendar'
        help    'todo.is.ui.planning.help'
        title   'todo.is.ui.planning'
        secured 'inProject() or (isAuthenticated() and stakeHolder())'
        menu {
            defaultPosition 3
            defaultVisibility true
        }
        embedded = [
                view: 'index',
                viewTypes: ['postits', 'notes', 'releaseBurndownChart', 'releaseParkingLotChart'],
                id: { project ->
                    def id = [label: message(code: 'is.release'), select: [[key: '', value: message(code: 'is.ui.releasePlan.id.empty')]]]
                    project.releases?.sort({ a, b -> a.orderNumber <=> b.orderNumber } as Comparator)?.each {
                        id.select << [key: it.id, value: "${it.name}"]
                    }
                    id
                }
        ]
    }
    'taskBoard' {
        details true
        context 'project'
        icon    'tasks'
        help    'todo.is.ui.taskBoard.help'
        title   'todo.is.ui.taskBoard'
        secured 'inProject() or (isAuthenticated() and stakeHolder())'
        menu {
            defaultPosition 4
            defaultVisibility true
        }
        embedded = [
                view : 'index',
                viewTypes: ['postits', 'table', 'notes', 'sprintBurndownRemainingChart', 'sprintBurnupTasksChart', 'sprintBurnupStoriesChart', 'sprintBurnupPointsChart'],
                id: { project ->
                    def id = [label: message(code: 'is.sprint'), select: [[key: '', value: message(code: 'is.ui.sprintPlan.id.empty')]]]
                    project.releases?.sort({ a, b -> a.orderNumber <=> b.orderNumber } as Comparator)?.each {
                        it.sprints?.collect { v -> id.select << [key: v.id, value: "${it.name} - Sprint ${v.index}"] }
                    }
                    id
                }
        ]
    }

}

/*
    'widgetName' {
        icon                        default: ''                   | String (fontawesome)
        title                       default: name                 | String (i18n key or ...)
        secured                     default: "permitAll()"        | String (spEl expression)
        context                     default: null                 | String (project or ...)
        context                     default: null                 | String (project or ...)
        ngController                default: null                 | String
        templatePath                default: "/widgets/widgetName/widget"| String (full path to template)

        allowDuplicate              default: true                 | true/false
        allowRemove                 default: true                 | true/false
        defaultSettings (=)         default: [:]                  | Map

        onSave                      default: nothing              | Closure(widgetInstance)
        onUpdate                    default: nothing              | Closure(widgetInstance, newSettingsValues)
        onDelete                    default: nothing              | Closure(widgetInstance)

        Others custom settings can be added as field and will be added to options property (Map [fieldName:fieldValue])

        Automatically:
        name                        is.ui.widget. widgetName .name
        help                        is.ui.widget. widgetName .help
        description                 is.ui.widget. widgetName .description
   }
 */

widgets = {
    'feed' {
        height 1
        width 2
        icon 'rss'
        title '{{ holder.feed.title }}'
        secured 'isAuthenticated()'
        ngController 'feedWidgetCtrl'
        defaultSettings = [
                feeds: [
                        [url: 'https://www.icescrum.com/blog/feed/', title: 'iceScrum', selected: true]
                ]
        ]
        onUpdate { widget, settings ->
            settings.feeds?.findAll { !it.title }?.each {
                try {
                    it.title = new XmlSlurper().parse(it.url).channel.title.text()
                } catch (Exception e) {}
            }
            settings.feeds = settings.feeds?.findAll { it.title }.unique { it.url }
        }
    }
    'login' {
        height 1
        width 2
        icon 'user'
        secured '!isAuthenticated()'
    }
    'notes' {
        height 1
        width 2
        icon 'pencil-square-o'
        secured 'isAuthenticated()'
        defaultSettings = [text: '']
        onUpdate { widget, settings ->
            settings.text_html = ServicesUtils.textileToHtml(settings.text)
        }
    }
    'publicProjects' {
        height 2
        width 3
        icon 'folder-open'
        allowDuplicate false
    }
    'tasks' {
        height 2
        width 2
        icon 'tasks'
        allowDuplicate false
        secured 'isAuthenticated()'
        ngController 'taskWidgetCtrl'
        defaultSettings = [
                postitSize: 'list-group'
        ]
    }
    'quickProjects' {
        height 2
        width 1
        icon 'folder'
        allowDuplicate false
        secured 'isAuthenticated()'
        ngController 'quickProjectsListCtrl'
    }
    'chart' {
        height 2
        width 2
        icon 'bar-chart'
        title '<a href="{{ getUrl() }}">{{ getTitle() }}</a>'
        secured 'isAuthenticated()'
        ngController 'projectChartWidgetCtrl'
        defaultSettings = [:]
    }
    'backlogChart' {
        height 1
        width 1
        icon 'pie-chart'
        title '<a href="{{ getUrl() }}">{{ getTitle() }}</a>'
        secured 'isAuthenticated()'
        ngController = "backlogChartWidgetCtrl"
        defaultSettings = [:]
    }
}