import React from 'react'
import { Switch, Route, Redirect } from 'react-router-dom'
import Projects from './Projects'
import Users from './Users'
import Tasks from './Tasks'
import AddTask from './AddTask'

const Main = () => (
  <main>
    <Switch>
      <Redirect exact from="/" to="/projects" />
      <Route exact path='/projects' component={Projects}/>
      <Route exact path='/users' component={Users}/>
      <Route exact path='/tasks' component={Tasks}/>
      <Route exact path='/addTask' component={AddTask}/>
    </Switch>
  </main>
)

export default Main
