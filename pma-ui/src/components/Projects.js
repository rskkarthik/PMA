import React from 'react';
import TextField from '@material-ui/core/TextField';
import Button from '@material-ui/core/Button';
import Switch from '@material-ui/core/Switch';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import axios from 'axios';
import Moment from 'react-moment';
import UpArrow from '@material-ui/icons/ArrowUpward'
import DownArrow from '@material-ui/icons/ArrowDownward'

import Dialog from '@material-ui/core/Dialog';
import DialogContent from '@material-ui/core/DialogContent';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';

const getCurrentDate = new Date().toJSON().slice(0,10);
const getNextDate = new Date(new Date().getTime() + (24 * 60 * 60 * 1000)).toJSON().slice(0,10);

let priorityOrder = false; 
let startDateOrder = false; 
let endDateOrder = false;
let completedOrder = false;
let currentSortedField = '';

axios.defaults.baseURL = 'http://localhost:8080/pma-services';

export default class Projects extends React.Component {

  constructor(props) {
    super(props);

    this.state = {
      id: "",
      projectName: "",
      priorityValue: 0,
      enableStartEndDate: true,
      startDate: getCurrentDate,
      endDate: getNextDate,
      active: true,
      
      searchStr: "",
      projectList: [],
      addUpdateBtnTxt: "Add",
      
      managerId: "",
      managerName: "",
      managerSearchStr: '',
      managerList: [],
      showManagerDialog: false
    };

    this.onInputChange = this.onInputChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this)
    this.handleClearForm = this.handleClearForm.bind(this)
    this.getProjects = this.getProjects.bind(this)
    this.sortByStartDate = this.sortByStartDate.bind(this)
    this.sortByEndDate = this.sortByEndDate.bind(this)
    this.sortByPriority = this.sortByPriority.bind(this)
    this.sortByCompleted = this.sortByCompleted.bind(this)
    this.searchProjects = this.searchProjects.bind(this);
    
    this.getUsers = this.getUsers.bind(this)
    this.searchUsers = this.searchUsers.bind(this)
    this.showManagerDialogBox = this.showManagerDialogBox.bind(this)
    this.hideManagerDialogBox = this.hideManagerDialogBox.bind(this)
  }
  
  componentDidMount() {
    this.getProjects();
    this.getUsers();
  }

  onInputChange(event, valueName) {
    this.setState({
      [valueName]: event.target.value
    });
  }

  handleSubmit(event) {
	  
	 if (this.state.addUpdateBtnTxt === "Update") {
	   this.updateProject(event);
	   return;
	 }
	 
     axios.post('/addProject', {
      name: this.state.projectName,
      priority: this.state.priorityValue,
      setDate: this.state.enableStartEndDate,
      startDate: this.state.startDate,
      endDate: this.state.endDate,
      managerId: this.state.managerId
    })
    .then((response) => {
      if (response.data.status === 200) {
  		this.handleClearForm();
  		this.getProjects();
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
  
  updateProject(event) {
    axios.post('/updateProject', {
      id: this.state.id,
      name: this.state.projectName,
      priority: this.state.priorityValue,
      setDate: this.state.enableStartEndDate,
      startDate: this.state.startDate,
      endDate: this.state.endDate,
      managerId: this.state.managerId,
      active: this.state.active
    })
    .then((response) => {
      if (response.data.status === 200) {
  		this.handleClearForm();
  		this.getProjects();
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

  setUpdateForm(project) {
    this.setState({
      id: project.id,
      projectName: project.name,
      managerId: project.managerId,
      managerName: project.managerName,
      priorityValue: project.priority,
      enableStartEndDate: project.setDate,
      startDate: project.startDate != null ? new Date(project.startDate).toJSON().slice(0,10) : '',
      endDate: project.endDate != null ? new Date(project.endDate).toJSON().slice(0,10) : '',
      active: project.active,
      addUpdateBtnTxt: "Update"
    });
  }

  getUsers() {
    axios
      .get("/getUsers")
      .then(response => {
    	  if (response.data.status === 200) {
    		this.setState({
    	      managerList: response.data.data
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
	if (this.state.managerSearchStr === "") {
	  this.getUsers();
	  return;
	}
    axios.post('/searchUsers', {
    	"name": this.state.managerSearchStr
    })
    .then((response) => {
      if (response.data.status === 200) {
        this.setState({
        	managerList: response.data.data
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
    if (this.state.searchStr === "") {
      this.getProjects();
      return;
    }
    axios
      .post("/searchProjects", {
        name: this.state.searchStr
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
  
  handleClearForm(event) {
    this.setState({
      id: '',
      projectName: '',
      managerId: '',
      managerName: '',
      priorityValue: 0,
      enableStartEndDate: true,
      startDate: getCurrentDate,
      endDate: getNextDate,
      active: true,
      addUpdateBtnTxt: "Add"
    });
  }

  suspendProject(projectId) {
	    axios
	      .post("/endProject", {
        id: projectId
      })
      .then(response => {
    	  if (response.data.status === 200) {
    		  this.handleClearForm();
    		  this.getProjects();
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
  
  handleCheckBox(event, valueName) {
    this.setState({ [valueName]: event.target.checked });
  }

  sortByStartDate() {
	currentSortedField = 'startDate';
    let data = this.state.projectList;
    startDateOrder = !startDateOrder;
    data.sort(function (a, b) {
      const x = a.startDate;
      const y = b.startDate;
      if (x == null && y == null) {
    	  return false;
      } else if (x == null) {
    	  return (startDateOrder ? false : true);
      } else if (y == null) {
    	  return (startDateOrder ? true : false);
      }
      return (startDateOrder ? x > y : x < y);
    });
    this.setState({
      projectList: data
    })
  }

  sortByEndDate() {
	currentSortedField = 'endDate';
	let data = this.state.projectList;
    endDateOrder = !endDateOrder;
    data.sort(function (a, b) {
      const x = a.endDate;
      const y = b.endDate;
      if (x == null && y == null) {
    	  return false;
      } else if (x == null) {
    	  return (endDateOrder ? false : true);
      } else if (y == null) {
    	  return (endDateOrder ? true : false);
      }
      return (endDateOrder ? x > y : x < y);
    });
    this.setState({
      projectList: data
    })
  }

  sortByPriority() {
	currentSortedField = 'priority';
    let data = this.state.projectList;
    priorityOrder = !priorityOrder;
    data.sort(function (a, b) {
      const x = a.priority;
      const y = b.priority;
      return (priorityOrder ? x > y : x < y);
    });
    this.setState({
      projectList: data
    })
  }

  sortByCompleted() {
	currentSortedField = 'completed';
    let data = this.state.projectList;
    completedOrder = !completedOrder;
    data.sort(function (a, b) {
      const x = a.active;
      const y = b.active;
      return (completedOrder ? x < y : x > y);
    });
    this.setState({
      projectList: data
    })
  }

  showManagerDialogBox = () => {
    this.setState({ showManagerDialog: true });
  };
  
  hideManagerDialogBox = () => {
    this.setState({ showManagerDialog: false });
  };  

  setSelectedUser(user) {
    this.setState({
	  showManagerDialog: false,
	  managerId: user.id,
	  managerName: user.firstName + ' ' + user.lastName
	})	  
  }

  render() {
    return (
      <div className='container'>
        <hr />
          <div id='rootFormDiv'>
            
          	<div id="projectNameRow" className='row justify-content-center'>
          	  <div className="col-1 mt-4">
          	  	Project:
          	  </div>
              <div className='col-11'>
                <TextField
                  id="projectName"
                  value={this.state.projectName}
                  onChange={event => this.onInputChange(event, 'projectName')}
                  margin="normal"
                  fullWidth={true}
                />
              </div>
            </div>

            <div id='datePicker' className='row justify-content-end mt-2'>
              <div className='col-3'>              
                <FormControlLabel
                  control={
                    <Switch
                      checked={this.state.enableStartEndDate}
                      onChange={event => this.handleCheckBox(event, 'enableStartEndDate')}
                      value={this.state.enableStartEndDate}
                      color="primary"
                    />
                  }
                  label="Set Start and End Date"
                />
              </div>
              <div className='col-2'>
                <TextField
                  id="startDate"
                  label="Start Date"
                  type="date"
                  value={this.state.startDate}
                  onChange={event => this.onInputChange(event, 'startDate')}
                  InputLabelProps={{
                    shrink: true,
                  }}
                  disabled={!this.state.enableStartEndDate}
                />
              </div>
              <div className='col-6'>
                <TextField
                  id="endDate"
                  label="End Date"
                  type="date"
                  value={this.state.endDate}
                  onChange={event => this.onInputChange(event, 'endDate')}
                  InputLabelProps={{
                    shrink: true,
                  }}
                  disabled={!this.state.enableStartEndDate}
                />
              </div>
            </div>
            
            <div id='priorityValueRow' className='row mt-3'>
              <div className='col-1'>
              </div>
              <div className='col-1'>
              	<p className="text-left">0</p>
              </div>
              <div className='col-9'>
              </div>
              <div className='col-1'>
              	<p className="text-right">30</p>
              </div>
              <div className='col-1'>
            	Priority:
              </div>
              <div className='col-11'>                
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

            <div id='managerIdRow' className='row'>
        	  <div className="col-1 mt-4">
        	  	Manager:
        	  </div>
        	  
        	  <div className="col-9">
        	    <TextField
                  margin="normal"
                  value={this.state.managerName}
                  fullWidth={true}
        	      InputProps={{readOnly: true}}
        	    />
        	  </div>
        	  
              <div className='col-2 mt-2'>
                <Button variant="outlined" color="primary" className='' onClick={this.showManagerDialogBox}>
                  Search
                </Button>
              </div>
            </div>


            
            <div id='btnRow' className='row justify-content-end mt-3'>            
                <div className='col-1'>
                  <Button 
                    type='submit' variant="contained" color="primary" className=''  
                    onClick={this.handleSubmit} 
                    disabled={!this.state.projectName || !this.state.managerId}>
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

            <hr/>
            
            <div id='sortingButtons' className='row mb-4'>
                <div className="col-12 mb-3">
                  <TextField
                    id="search"
                    label="Search for projects (type and enter)"
                    margin="normal"
                    value={this.state.searchStr}
                    onChange={event => this.onInputChange(event, "searchStr")}
                    fullWidth={true}
                    onKeyPress={this.searchProjects} />
                </div>
                <div className="col-1 mt-2">
                	Sort By:
                </div>
                <div className='col-2'>
                  <Button  id='startDateSort' variant="outlined" color="primary" className="fa fa-arrow-down" onClick={this.sortByStartDate}>
                    Start Date 
                    {currentSortedField === 'startDate' && (startDateOrder ? <UpArrow/> : <DownArrow/>)}
                  </Button>
                </div>
                <div className='col-2'>
                  <Button  id='endDateSort' variant="outlined" color="primary" className='' onClick={this.sortByEndDate}>
                    End Date
                    {currentSortedField === 'endDate' && (endDateOrder ? <UpArrow/> : <DownArrow/>)}
                  </Button>
                </div>
                <div className='col-2'>
                  <Button  id='prioritySort' variant="outlined" color="primary" className='' onClick={this.sortByPriority}>
                    Priority
                    {currentSortedField === 'priority' && (priorityOrder ? <UpArrow/> : <DownArrow/>)}
                  </Button>
                </div>
                <div className='col-2'>
                  <Button  id='completedSort' variant="outlined" color="primary" className='' onClick={this.sortByCompleted}>
                    Completed
                    {currentSortedField === 'completed' && (completedOrder ? <UpArrow/> : <DownArrow/>)}
                  </Button>
              </div>
            </div>

            {/* Iterate the project list */}
            {this.state.projectList.map((list, index) => (
              <div id='projectInfoMain' className='card' key={index}>
                <div className='card-body'>
                
                  <div className='row'>
                  
                      <div className='col-6' id='projectInfo'>
                      
                        <div className='row'>
                        
                          <div className='col-12 mb-3' id='projectName'>
                            Project: {list.name}
                          </div>
                          
                          <div className='col-6' id='numTasks'>
                            Number of Tasks:  {list.totalTaskCount}
                          </div>
                          
                          <div className='col-6' id='completedTasks'>
                            Completed: {list.completedTaskCount}
                          </div>
                          
                          <div className='col-6' id='startDate'>
                            {list.startDate ?
                              <p>Start Date: <Moment format="MM-DD-YYYY">{list.startDate}</Moment></p>
                              :
                              <p>Start Date: N/A</p>
                            }
                          </div>
                          
                          <div className='col-6' id='endDate'>
                            {list.endDate ?
                              <p>End Date: <Moment format="MM-DD-YYYY">{list.endDate}</Moment></p>
                              :
                              <p>End Date: N/A</p>
                            }
                          </div>
                          
                          <div className='col-12' id='managerName'>
                          	<p>Manager: {list.managerName}</p>
                          </div>
                          
                        </div>
                        
                      </div>

                      <div className='col-2'>
                      	<div className='row'>
                      		<div className='col-12 mb-3'>
                      			Priority
                      		</div>
                      		<div className='col-12'>
                      			{list.priority}
                      		</div>
                      	</div>
                      </div>
                      
                      <div className='col-2'>
                    	<div className='row'>
                    		<div className='col-12 mb-3'>
                    			Status
                    		</div>
                    		<div className='col-12'>
                    			{list.active ? 'Active' : 'Suspended'}
                    		</div>
                    	</div>
                      </div>

                      <div className='col-2'>
                      	<Button
                      		type="submit"
                      		variant="outlined"
                      		color="primary"
                      		className=""
                      		onClick={() => this.setUpdateForm(list)}>
                      	  Update
                      	</Button>
                      	{list.active && 
                      	  <Button
                      		type="submit"
                      		variant="outlined"
                      		color="primary"
                      		className=""
                      		onClick={() => this.suspendProject(list.id)}>
                      	    Suspend
                      	  </Button>
                      	}
                      </div>
                  
                   </div>
                   
                </div>
              </div>
            ))
            }
                  
          </div>
          
          {/* Dialog Related code */}

          <div id='managerDialogBox'>
            <Dialog
              open={this.state.showManagerDialog}
              onClose={this.hideManagerDialogBox}
              >
              <DialogContent>
	              <div className="row">
	              	<div className="col-12">
	              		<TextField
	              			id="search"
	              			label="Search user..."
	              			margin="normal"
	              			value={this.state.managerSearchStr}
	              			onChange={event => this.onInputChange(event, "managerSearchStr")}
	              			fullWidth={true}
	              			onKeyPress={this.searchUsers} />
	              	</div>
	                <div className="col-12" style={{height: '400px', overflow: 'auto'}}>
	                	<List component="nav">
	                		{this.state.managerList.map(option => (
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
          
      </div>
    )
  };
}
