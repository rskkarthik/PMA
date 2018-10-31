import React from 'react';
import TextField from '@material-ui/core/TextField';
import Button from '@material-ui/core/Button';
import Switch from '@material-ui/core/Switch';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import axios from 'axios';

import Dialog from '@material-ui/core/Dialog';
import DialogContent from '@material-ui/core/DialogContent';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';

const getCurrentDate = new Date().toJSON().slice(0,10);
const getNextDate = new Date(new Date().getTime() + (24 * 60 * 60 * 1000)).toJSON().slice(0,10);

axios.defaults.baseURL = 'http://localhost:8080/pma-services';

export default class AddTask extends React.Component {

  constructor(props) {
    super(props);

    this.state = {
      id: "",
      taskName: "",
      priorityValue: "0",
      startDate: getCurrentDate,
      endDate: getNextDate,
      active: true,
      
      projectId: '',
      projectName: '',
      projectSearchStr: '',
      projectList: [],
      showProjectDialog: false,

      setParentTask: true,
      parentTaskId: '',
      parentTaskName: '',
      parentTaskSearchStr: '',
      parentTaskList: [],
      showParentTaskDialog: false,
      
      userId: '',
      userName: '',
      userSearchStr: '',
      userList: [],
      showUserDialog: false,
      
      addUpdateBtnTxt: "Add"
    };

    this.onInputChange = this.onInputChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this)
    this.handleClearForm = this.handleClearForm.bind(this)
    
    this.getProjects = this.getProjects.bind(this)
    this.searchProjects = this.searchProjects.bind(this)
    this.showProjectDialogBox = this.showProjectDialogBox.bind(this)
    this.hideProjectDialogBox = this.hideProjectDialogBox.bind(this)
    
    this.getTasks = this.getTasks.bind(this)
    this.searchTasks = this.searchTasks.bind(this)
    this.showParentTaskDialogBox = this.showParentTaskDialogBox.bind(this)
    this.hideParentTaskDialogBox = this.hideParentTaskDialogBox.bind(this)
    
    this.getUsers = this.getUsers.bind(this)
    this.searchUsers = this.searchUsers.bind(this)
    this.showUserDialogBox = this.showUserDialogBox.bind(this)
    this.hideUserDialogBox = this.hideUserDialogBox.bind(this)
  }
  
  componentDidMount() {
	  if (this.props.location.state 
			  && this.props.location.state.updateTask) {
		  this.setUpdateForm(this.props.location.state.updateTask);
	  }
	  
	  this.getProjects();
	  this.getUsers();
	  this.getTasks();
  }

  onInputChange(event, valueName) {
    this.setState({
      [valueName]: event.target.value
    });
  }

  handleSubmit(event) {
	  
	 if (this.state.addUpdateBtnTxt === "Update") {
	   this.updateTask(event);
	   return;
	 }	  
	 
     axios.post('/addTask', {
       name: this.state.taskName,
       priority: this.state.priorityValue,
       startDate: this.state.startDate,
       endDate: this.state.endDate,
       projectId: this.state.projectId,
       userId: this.state.userId,
       setParentTask: this.state.setParentTask,
       parentTaskId: this.state.parentTaskId,
       active: this.state.active
    })
    .then((response) => {
      if (response.data.status === 200) {
    	alert("Task '" + this.state.taskName + "' added successfully!");
  		this.handleClearForm();
  	  } else if (response.data.status === 202) {
  		alert(response.data.message);
  	  } else if (response.data.status === 500) {
  		alert('Internal server error!');
  	  }
    })
    .catch(function (error) {
      alert('Internal server error!');      
    });
  }
  
  updateTask(event) {
     axios.post('/updateTask', {
    	 id: this.state.id,
         name: this.state.taskName,
         priority: this.state.priorityValue,
         startDate: this.state.startDate,
         endDate: this.state.endDate,
         projectId: this.state.projectId,
         userId: this.state.userId,
         setParentTask: this.state.setParentTask,
         parentTaskId: this.state.parentTaskId,
         active: this.state.active
      })
      .then((response) => {
        if (response.data.status === 200) {
    		this.handleClearForm();
    	    this.props.history.push({
    	      pathname: "/tasks"
            });
    	  } else if (response.data.status === 202) {
    		alert(response.data.message);
    	  } else if (response.data.status === 500) {
    		alert('Internal server error!');
    	  }
      })
      .catch(function (error) {
        alert('Internal server error!');      
      });
  }

  setUpdateForm(task) {
    this.setState({
      id: task.id,
      taskName: task.name,
      priorityValue: task.priority,
      startDate: new Date(task.startDate).toJSON().slice(0,10),
      endDate: new Date(task.endDate).toJSON().slice(0,10),
      projectId: task.projectId,
      projectName: task.projectName,
      userId: task.userId,
      userName: task.userName,
      setParentTask: task.setParentTask,
      parentTaskId: task.parentTaskId,
      parentTaskName: task.parentTaskName,
      active: task.active,
      addUpdateBtnTxt: "Update"
    });
  }
  
  handleClearForm(event) {
    this.setState({
    	id: "",
        taskName: "",
        priorityValue: 0,
        startDate: getCurrentDate,
        endDate: getNextDate,
        active: true,
        
        projectId: '',
        projectName: '',

        setParentTask: true,
        parentTaskId: '',
        parentTaskName: '',
        
        userId: '',
        userName: '',
        
        addUpdateBtnTxt: "Add"
    });
  }

  handleCheckBox(event, valueName) {
    this.setState({ [valueName]: event.target.checked });
  }

  getUsers() {
    axios
      .get("/getUsers")
      .then(response => {
    	  if (response.data.status === 200) {
    		this.setState({
    	      userList: response.data.data
    	    });
    	  } else if (response.data.status === 202) {
    		  alert(response.data.message);
    	  } else if (response.data.status === 500) {
    		  alert('Internal server error!');
    	  }
      })
      .catch(function(error) {
        alert("Internal server error!");
      });
  }
  
  searchUsers(event) {
    if (event.charCode !== 13) {
	  return;
	}
	if (this.state.userSearchStr === "") {
	  this.getUsers();
	  return;
	}
    axios.post('/searchUsers', {
    	"name": this.state.userSearchStr
    })
    .then((response) => {
      if (response.data.status === 200) {
        this.setState({
        	userList: response.data.data
        })
      } else if (response.data.status === 202) {
        alert(response.data.message);
      } else if (response.data.status === 500) {
        alert('Internal server error!');
      }
    })
    .catch(function (error) {
      alert("Internal server error!");
    });
  }

  showUserDialogBox = () => {
    this.setState({ showUserDialog: true });
  };
  
  hideUserDialogBox = () => {
    this.setState({ showUserDialog: false });
  };  
  
  setSelectedUser(user) {
    this.setState({
	  showUserDialog: false,
	  userId: user.id,
	  userName: user.firstName + ' ' + user.lastName
	})	  
  }
  
  getProjects() {
    axios.get('/getProjects')
    .then((response) => {
      if (response.data.status === 200) {
    	this.setState({
          projectList: response.data.data
    	});
      } else if (response.data.status === 202) {
        alert(response.data.message);
      } else if (response.data.status === 500) {
        alert('Internal server error!');
      }
    })
    .catch(function (error) {
    	alert('Internal server error!');
    });
  }

  searchProjects(event) {
    if (event.charCode !== 13) {
      return;
    }
    if (this.state.projectSearchStr === "") {
      this.getProjects();
      return;
    }
    axios
      .post("/searchProjects", {
        name: this.state.projectSearchStr
      })
      .then(response => {
    	  if (response.data.status === 200) {
    		this.setState({
    	      projectList: response.data.data
    	    });
      	  } else if (response.data.status === 202) {
      		  alert(response.data.message);
      	  } else if (response.data.status === 500) {
      		  alert('Internal server error!');
      	  }
      })
      .catch(function(error) {
      alert("Internal server error!");
    });
  }

  showProjectDialogBox = () => {
    this.setState({ showProjectDialog: true });
  };
  
  hideProjectDialogBox = () => {
    this.setState({ showProjectDialog: false });
  };  
  
  setSelectedProject(project) {
    this.setState({
	  showProjectDialog: false,
	  projectId: project.id,
	  projectName: project.name
	})	  
  }
  
  getTasks() {
    axios
      .get("/getTasks")
      .then(response => {
    	  if (response.data.status === 200) {
    		this.setState({
    	      parentTaskList: response.data.data
    	    });
    	  } else if (response.data.status === 202) {
    		  alert(response.data.message);
    	  } else if (response.data.status === 500) {
    		  alert('Internal server error!');
    	  }
      })
      .catch(function(error) {
        alert("Internal server error!");
      });
  }
  
  searchTasks(event) {
    if (event.charCode !== 13) {
	  return;
	}
	if (this.state.parentTaskSearchStr === "") {
	  this.getUsers();
	  return;
	}
    axios.post('/searchUsers', {
    	"name": this.state.parentTaskSearchStr
    })
    .then((response) => {
      if (response.data.status === 200) {
        this.setState({
        	parentTaskList: response.data.data
        })
      } else if (response.data.status === 202) {
        alert(response.data.message);
      } else if (response.data.status === 500) {
        alert('Internal server error!');
      }
    })
    .catch(function (error) {
      alert("Internal server error!");
    });
  }

  showParentTaskDialogBox = () => {
    this.setState({ showParentTaskDialog: true });
  };
  
  hideParentTaskDialogBox = () => {
    this.setState({ showParentTaskDialog: false });
  };  
  
  setSelectedParentTask(parentTask) {
    this.setState({
	  showParentTaskDialog: false,
	  parentTaskId: parentTask.id,
	  parentTaskName: parentTask.name
	})	  
  }

  
  render() {
    return (
      <div className='container'>
        <hr />
          <div id='rootFormDiv'>
          
          <div id="projectNameRow" className='row justify-content-center'>
      	  <div className="col-2 mt-4">
      	  	Project:
      	  </div>
          <div className='col-8'>
            <TextField
              id="projectName"
              value={this.state.projectName}
              onChange={event => this.onInputChange(event, 'projectName')}
              margin="normal"
              fullWidth={true}
              InputProps={{readOnly: true}}
            />
          </div>
            <div className='col-2 mt-2'>
            <Button variant="outlined" color="primary" className='' onClick={this.showProjectDialogBox} >
              Search
            </Button>
          </div>
          </div>
            
          	<div id="taskNameRow" className='row justify-content-center mt-3'>
          	  <div className="col-2 mt-4">
          	  	Task:
          	  </div>
              <div className='col-10'>
                <TextField
                  id="taskName"
                  value={this.state.taskName}
                  onChange={event => this.onInputChange(event, 'taskName')}
                  margin="normal"
                  fullWidth={true}
                />
              </div>
            </div>
            
            <div id='setParentTaskRow' className='row justify-content-end mt-2'>
            <div className='col-10'>              
              <FormControlLabel
                control={
                  <Switch
                    checked={this.state.setParentTask}
                    onChange={event => this.handleCheckBox(event, 'setParentTask')}
                    value={this.state.setParentTask}
                    color="primary"
                  />
                }
                label="Parent Task"
              />
              </div>
              </div>
              
              <div id="parentTaskRow" className='row justify-content-center'>
          	  <div className="col-2 mt-4">
          	  	Parent Task:
          	  </div>
              <div className='col-8'>
                <TextField
                  id="parentTaskName"
                  value={this.state.parentTaskName}
                  onChange={event => this.onInputChange(event, 'parentTaskName')}
                  margin="normal"
                  fullWidth={true}
                  InputProps={{readOnly: true}}
                />
              </div>
                <div className='col-2 mt-2'>
                <Button variant="outlined" color="primary" className='' onClick={this.showParentTaskDialogBox} disabled={!this.state.setParentTask} >
                  Search
                </Button>
              </div>
              </div>
              
              <div id='priorityValueRow' className='row mt-4'>
              <div className='col-2'>
              </div>
              <div className='col-1'>
              	<p className="text-left">0</p>
              </div>
              <div className='col-8'>
              </div>
              <div className='col-1'>
              	<p className="text-right">30</p>
              </div>
              <div className='col-2'>
            	Priority:
              </div>
              <div className='col-10'>                
                <input 
                  id="priorityValue" 
                  className='form-control-range'
                  type="range" 
                  min="0" max="30" 
                  value={this.state.priorityValue} 
                  onChange={event => this.onInputChange(event, 'priorityValue')}
                  step="1" />
                </div>              
            </div> 
            
            
            <div id='dateRow' className='row justify-content-end mt-5'>
            <div className='col-2'>
            	Start Date:
            </div>
            <div className='col-4'>
              <TextField
                id="startDate"
                type="date"
                value={this.state.startDate}
                onChange={event => this.onInputChange(event, 'startDate')}
                InputLabelProps={{
                  shrink: true,
                }}
              />
            </div>
              <div className='col-2'>
             	End Date:
              </div>
            <div className='col-4'>
              <TextField
                id="endDate"
                type="date"
                value={this.state.endDate}
                onChange={event => this.onInputChange(event, 'endDate')}
                InputLabelProps={{
                  shrink: true,
                }}
              />
            </div>
          </div>
            
            
            <div id="userRow" className='row justify-content-center mt-3'>
        	  <div className="col-2 mt-4">
        	  	User:
        	  </div>
            <div className='col-8'>
              <TextField
                id="userName"
                value={this.state.userName}
                onChange={event => this.onInputChange(event, 'userName')}
                margin="normal"
                fullWidth={true}
                InputProps={{readOnly: true}}
              />
            </div>
              <div className='col-2 mt-2'>
              <Button variant="outlined" color="primary" className='' onClick={this.showUserDialogBox} >
                Search
              </Button>
            </div>
            </div>
              
              <hr className="mt-3"/>
              
            <div id='btnRow' className='row justify-content-end mt-4'>            
                <div className='col-1'>
                  <Button 
                    type='submit' variant="contained" color="primary" className=''  
                    onClick={this.handleSubmit} 
                    disabled={!this.state.projectId 
                		  || !this.state.taskName
                		  || !this.state.priorityValue
                		  || !this.state.startDate
                		  || !this.state.endDate
                		  || !this.state.userId
                		  }>
                    {this.state.addUpdateBtnTxt}
                  </Button>
                </div>
                <div className='col-1'>
                <Button type='reset' id='cancelBtn' variant="outlined" color="primary" className='' onClick={this.handleClearForm}>
                  RESET
                </Button>
              </div>
                <div className='col-1'></div>
            </div>
                  
          </div>
          
          
          <div id='userDialogBox'>
          <Dialog
            open={this.state.showUserDialog}
            onClose={this.hideUserDialogBox}>
            <DialogContent>
	              <div className="row">
	              	<div className="col-12">
	              		<TextField
	              			id="search"
	              			label="Search user..."
	              			margin="normal"
	              			value={this.state.userSearchStr}
	              			onChange={event => this.onInputChange(event, "userSearchStr")}
	              			fullWidth={true}
	              			onKeyPress={this.searchUsers} />
	              	</div>
	                <div className="col-12" style={{height: '400px', overflow: 'auto'}}>
	                	<List component="nav">
	                		{this.state.userList.map(option => (
	                			<ListItem button key={option.id}>
	                				<ListItemText onClick={() => this.setSelectedUser(option)}>{option.firstName} {option.lastName}</ListItemText>
	                			</ListItem>
	                		))}
	                	</List>
	               </div>
	             </div>
            </DialogContent>
          </Dialog>
        </div>
        
        
        
	        <div id='projectDialogBox'>
	        <Dialog
	          open={this.state.showProjectDialog}
	          onClose={this.hideProjectDialogBox}>
	          <DialogContent>
	              <div className="row">
	              	<div className="col-12">
	              		<TextField
	              			id="search"
	              			label="Search project..."
	              			margin="normal"
	              			value={this.state.projectSearchStr}
	              			onChange={event => this.onInputChange(event, "projectSearchStr")}
	              			fullWidth={true}
	              			onKeyPress={this.searchProjectss} />
	              	</div>
	                <div className="col-12" style={{height: '400px', overflow: 'auto'}}>
	                	<List component="nav">
	                		{this.state.projectList.map(option => (
	                			<ListItem button key={option.id}>
	                				<ListItemText onClick={() => this.setSelectedProject(option)}>{option.name}</ListItemText>
	                			</ListItem>
	                		))}
	                	</List>
	               </div>
	             </div>
	          </DialogContent>
	        </Dialog>
	      </div>
      
      
      
	      <div id='parentTaskDialogBox'>
	      <Dialog
	        open={this.state.showParentTaskDialog}
	        onClose={this.hideParentTaskDialogBox}>
	        <DialogContent>
	            <div className="row">
	            	<div className="col-12">
	            		<TextField
	            			id="search"
	            			label="Search task..."
	            			margin="normal"
	            			value={this.state.parentTaskSearchStr}
	            			onChange={event => this.onInputChange(event, "parentTaskSearchStr")}
	            			fullWidth={true}
	            			onKeyPress={this.searchParentTask} />
	            	</div>
	              <div className="col-12" style={{height: '400px', overflow: 'auto'}}>
	              	<List component="nav">
	              		{this.state.parentTaskList.map(option => (
	              			<ListItem button key={option.id}>
	              				<ListItemText onClick={() => this.setSelectedParentTask(option)}>{option.name}</ListItemText>
	              			</ListItem>
	              		))}
	              	</List>
	             </div>
	           </div>
	        </DialogContent>
	      </Dialog>
	    </div>      
          
          
          
      </div>
    )
  };
}
