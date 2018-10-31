import React from 'react'
import { NavLink   } from 'react-router-dom'

const Header = () => (
  
  <div className='container' >
    <div className='row justify-content-left'>
      <h2>Project Manager</h2>
    </div>
    <div className='' >
      <ul className="nav justify-content-center">
        <li className="nav-item">
          <NavLink to='/projects' className="nav-link" activeClassName="selected" activeStyle={{fontWeight: "bold",color: "blue"}}>PROJECTS</NavLink>
        </li>
        <li className="nav-item">
          <NavLink to='/users' className="nav-link" activeClassName="active" activeStyle={{fontWeight: "bold",color: "blue"}}>USERS</NavLink>
        </li>
        <li className="nav-item">
          <NavLink to='/tasks' className="nav-link" activeClassName="active" activeStyle={{fontWeight: "bold",color: "blue"}}>TASKS</NavLink>
        </li>
        <li className="nav-item">
          <NavLink to='/addTask' className="nav-link" activeClassName="active" activeStyle={{fontWeight: "bold",color: "blue"}}>ADD TASK</NavLink>
        </li>
      </ul>
    </div>
  </div>
)

export default Header
