import React from 'react';
import TextField from '@material-ui/core/TextField';
import Button from '@material-ui/core/Button';
import axios from 'axios';
import Moment from 'react-moment';
import UpArrow from '@material-ui/icons/ArrowUpward'
import DownArrow from '@material-ui/icons/ArrowDownward'

let startDateOrder = false; 
let endDateOrder = false;
let priorityOrder = false;
let completedOrder = false;
let currentSortedField = '';

axios.defaults.baseURL = 'http://localhost:8080/pma-services';

export default class Tasks extends React.Component {

  constructor(props) {
    super(props);

    this.state = {
      id: "",
      searchStr: "",
      taskList: [],
    };

    this.onInputChange = this.onInputChange.bind(this);
    this.sortByStartDate = this.sortByStartDate.bind(this)
    this.sortByEndDate = this.sortByEndDate.bind(this)
    this.sortByPriority = this.sortByPriority.bind(this)
    this.sortByCompleted = this.sortByCompleted.bind(this)
    this.getTasks = this.getTasks.bind(this);
    this.searchTasks = this.searchTasks.bind(this);
  }
  
  componentDidMount() {
    this.getTasks();
  }

  onInputChange(event, valueName) {
    this.setState({
      [valueName]: event.target.value
    });
  }

  getTasks() {
    axios.get('/getTasks')
    .then((response) => {
      if (response.data.status === 202) {
        alert(response.data.message);
      } else if (response.data.status === 500) {
        alert('Internal server error!');
      }
      this.setState({
        taskList: response.data.data
      });
    })
    .catch(function (error) {
    	alert('Internal server error!');
    });
  }

  searchTasks(event) {
    if (event.charCode !== 13) {
      return;
    }
    if (this.state.searchStr === "") {
      this.getTasks();
      return;
    }
    axios
      .post("/searchTasks", {
        name: this.state.searchStr
      })
      .then(response => {
    	  if (response.data.status === 200) {
    		this.setState({
    	      taskList: response.data.data
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
  
  updateTask(task) {
    this.props.history.push({
      pathname: "/addTask",
      state: { updateTask: task }
    });
  }
  
  endTask(taskId) {
	    axios.post("/endTask", {
        id: taskId
      })
      .then(response => {
    	  if (response.data.status === 200) {
    		  this.getTasks();
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
  
  sortByStartDate() {
	currentSortedField = 'startDate';
    let data = this.state.taskList;
    startDateOrder = !startDateOrder;
    data.sort(function (a, b) {
      const x = a.startDate;
      const y = b.startDate;
      return (startDateOrder ? x > y : x < y);
    });
    this.setState({
      taskList: data
    })
  }

  sortByEndDate() {
	currentSortedField = 'endDate';
	let data = this.state.taskList;
    endDateOrder = !endDateOrder;
    data.sort(function (a, b) {
      const x = a.endDate;
      const y = b.endDate;
      return (endDateOrder ? x > y : x < y);
    });
    this.setState({
      taskList: data
    })
  }

  sortByPriority() {
	currentSortedField = 'priority';
    let data = this.state.taskList;
    priorityOrder = !priorityOrder;
    data.sort(function (a, b) {
      const x = a.priority;
      const y = b.priority;
      return (priorityOrder ? x > y : x < y);
    });
    this.setState({
      taskList: data
    })
  }

  sortByCompleted() {
	currentSortedField = 'completed';
    let data = this.state.taskList;
    completedOrder = !completedOrder;
    data.sort(function (a, b) {
      const x = a.active;
      const y = b.active;
      return (completedOrder ? x < y : x > y);
    });
    this.setState({
      taskList: data
    })
  }

  render() {
    return (
      <div className='container'>
        <hr />
          <div id='rootFormDiv'>
            
            <div id='sortingButtons' className='row mb-4'>
            	
                <div className="col-3">
                  <TextField
                    id="search"
                    label="Search..."
                    margin="normal"
                    value={this.state.searchStr}
                    onChange={event => this.onInputChange(event, "searchStr")}
                    fullWidth={true}
                    onKeyPress={this.searchTasks} />
                </div>
                  
                <div className="col-1 mt-4">
                	Sort By:
                </div>
                
                <div className='col-2 mt-3'>
                  <Button  id='startDateSort' variant="outlined" color="primary" className="fa fa-arrow-down" onClick={this.sortByStartDate}>
                    Start Date 
                    {currentSortedField === 'startDate' && (startDateOrder ? <UpArrow/> : <DownArrow/>)}
                  </Button>
                </div>
                
                <div className='col-2 mt-3'>
                  <Button  id='endDateSort' variant="outlined" color="primary" className='' onClick={this.sortByEndDate}>
                    End Date
                    {currentSortedField === 'endDate' && (endDateOrder ? <UpArrow/> : <DownArrow/>)}
                  </Button>
                </div>
                
                <div className='col-2 mt-3'>
                  <Button  id='prioritySort' variant="outlined" color="primary" className='' onClick={this.sortByPriority}>
                    Priority
                    {currentSortedField === 'priority' && (priorityOrder ? <UpArrow/> : <DownArrow/>)}
                  </Button>
                </div>
                
                <div className='col-2 mt-3'>
                  <Button  id='completedSort' variant="outlined" color="primary" className='' onClick={this.sortByCompleted}>
                    Completed
                    {currentSortedField === 'completed' && (completedOrder ? <UpArrow/> : <DownArrow/>)}
                  </Button>
                </div>
	                
            </div>

            {this.state.taskList.map((list, index) => (
              <div id='taskInfoMain' className='card' key={index}>
                <div className='card-body'>
                
                  <div className='row'>
                  
                  	<div className="col-2">
                  	  <p className="font-weight-bold">Task</p>
                  	  <p>{list.name}</p>
                  	</div>
                  	
                  	<div className="col-2">
                  	  <p className="font-weight-bold">Parent Task</p>
                  	  <p>{list.parentTaskName ? list.parentTaskName : '-'}</p>
              	    </div>
                  	
              	    <div className="col-1">
              	      <p className="font-weight-bold">Priority</p>
          		      <p>{list.priority}</p>
          	        </div>
                                        
              	    <div className="col-2">
              	      <p className="font-weight-bold">Start</p>
        		      <p><Moment format="MM-DD-YYYY">{list.startDate}</Moment></p>
        	        </div>
              	    
        	        <div className="col-2">
        	          <p className="font-weight-bold">End</p>
        	          <p><Moment format="MM-DD-YYYY">{list.endDate}</Moment></p>
        	        </div>

        	        <div className="col-2">
      	              <p className="font-weight-bold">Status</p>
      	              <p>{list.active ? 'Active' : 'Completed'}</p>
      	            </div>

      	            <div className="col-1">
      	              <Button
            		    type="submit"
                 		variant="outlined"
                		color="primary"
                		className=""
                		onClick={() => this.updateTask(list)}>
            	        Edit
            	      </Button>
            	      {list.active && 
            	        <Button
            		      type="submit"
                          variant="outlined"
            		      color="primary"
            		      className=""
            		      onClick={() => this.endTask(list.id)}>
            	          End
            	        </Button>
            	      }
        	        </div>
                   
        	      </div>
                   
                </div>
              </div>
            ))
            }
                  
          </div>
      </div>
    )
  };
}
