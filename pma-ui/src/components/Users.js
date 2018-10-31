import React from "react";
import TextField from "@material-ui/core/TextField";
import Button from "@material-ui/core/Button";
import axios from "axios";
import UpArrow from '@material-ui/icons/ArrowUpward'
import DownArrow from '@material-ui/icons/ArrowDownward'

let firstNameOrder = false; 
let lastNameOrder = false; 
let empIdOrder = false;
let currentSortedField = '';

axios.defaults.baseURL = 'http://localhost:8080/pma-services';

export default class Users extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      id: "",
      firstName: "",
      lastName: "",
      empId: "",
      userList: [],
      searchStr: "",
      addUpdateBtnTxt: "Add"
    };

    this.onInputChange = this.onInputChange.bind(this);
    this.addUser = this.addUser.bind(this);
    this.handleClearForm = this.handleClearForm.bind(this);
    this.getUsers = this.getUsers.bind(this);
    this.searchUsers = this.searchUsers.bind(this);
  }

  componentDidMount() {
    this.getUsers();
  }

  onInputChange(event, valueName) {
    this.setState({
      [valueName]: event.target.value
    });
  }

  addUser(event) {
    if (this.state.addUpdateBtnTxt === "Update") {
      this.updateUser(event);
      return;
    }

    axios
      .post("/addUser", {
        firstName: this.state.firstName,
        lastName: this.state.lastName,
        empId: this.state.empId
      })
      .then(response => {
    	  if (response.data.status === 200) {
    		  this.handleClearForm();
    		  this.getUsers();
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

  updateUser(event) {
    axios
      .post("/updateUser", {
        id: this.state.id,
        firstName: this.state.firstName,
        lastName: this.state.lastName,
        empId: this.state.empId
      })
      .then(response => {
    	  if (response.data.status === 200) {
    		  this.handleClearForm();
    		  this.getUsers();
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
        this.setState({
          userList: response.data.data
        });
      })
      .catch(function(error) {
        alert("Internal server error!");
      });
  }

  setUpdateForm(user) {
    this.setState({
      id: user.id,
      firstName: user.firstName,
      lastName: user.lastName,
      empId: user.empId,
      addUpdateBtnTxt: "Update"
    });
  }

  deleteUser(userId) {
    axios
      .post("/deleteUser", {
        id: userId
      })
      .then(response => {
		  if (response.data.status === 200) {
			  this.getUsers();
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
    if (this.state.searchStr === "") {
      this.getUsers();
      return;
    }
    axios
      .post("/searchUsers", {
        name: this.state.searchStr
      })
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

  handleClearForm(event) {
    this.setState({
      id: "",
      firstName: "",
      lastName: "",
      empId: "",
      addUpdateBtnTxt: "Add"
    });
  }

  handleResponse(response) {
    if (response.data.status === 202) {
      alert(response.data.message);
    } else if (response.data.status === 500) {
      alert("Internal server error!");
    }
  }

  sortByFirstName() {
    let data = this.state.userList;
    currentSortedField = 'firstName';
    firstNameOrder = !firstNameOrder;
    data.sort(function(a, b) {
      var nameA = a.firstName.toUpperCase();
      var nameB = b.firstName.toUpperCase();
      if (nameA < nameB) {
        return firstNameOrder? -1 : 1;
      }
      if (nameA > nameB) {
    	  return firstNameOrder? 1 : -1;
      }
      return 0;
    });
    this.setState({
      userList: data
    });
  }

  sortByLastName() {
    let data = this.state.userList;
    currentSortedField = 'lastName';
    lastNameOrder = !lastNameOrder;
    data.sort(function(a, b) {
      var nameA = a.lastName.toUpperCase();
      var nameB = b.lastName.toUpperCase();
      if (nameA < nameB) {
    	  return lastNameOrder? -1 : 1;
      }
      if (nameA > nameB) {
    	  return lastNameOrder? 1 : -1;
      }
      return 0;
    });
    this.setState({
      userList: data
    });
  }

  sortByEmpId() {
    let data = this.state.userList;
    currentSortedField = 'empId';
    empIdOrder = !empIdOrder;
    data.sort(function(a, b) {
      var x = a.empId;
      var y = b.empId;
      return empIdOrder ? x > y : x < y;
    });
    this.setState({
      userList: data
    });
  }

  render() {
    return (
      <div className="container">
        <hr />

        <div id="rootFormDiv">
          <div id="firstNameRow" className="row justify-content-center">
            <div className="col-2 mt-4">
              <p>First Name: </p>
            </div>
            <div className="col-10">
              <TextField
                id="firstName"
                value={this.state.firstName}
                onChange={event => this.onInputChange(event, "firstName")}
                margin="normal"
                fullWidth={true}
              />
            </div>
          </div>

          <div id="lastNameRow" className="row justify-content-center">
            <div className="col-2 mt-4">
              <p>Last Name: </p>
            </div>
            <div className="col-10">
              <TextField
                id="lastName"
                value={this.state.lastName}
                onChange={event => this.onInputChange(event, "lastName")}
                margin="normal"
                fullWidth={true}
              />
            </div>
          </div>

          <div id="empIdRow" className="row justify-content-center">
            <div className="col-2 mt-4">
              <p>Employee ID: </p>
            </div>
            <div className="col-10">
              <TextField
                id="empId"
                value={this.state.empId}
                onChange={event => this.onInputChange(event, "empId")}
                margin="normal"
                fullWidth={true}
              />
            </div>
          </div>

          <div id="btnRow" className="row justify-content-end mt-3">
            <div className="col-1">
              <Button
                type="submit"
                variant="contained"
                color="primary"
                className=""
                onClick={this.addUser}
                disabled={
                  !this.state.firstName ||
                  !this.state.lastName ||
                  !this.state.empId
                }
              >
                {this.state.addUpdateBtnTxt}
              </Button>
            </div>

            <div className="col-2">
              <Button
                type="reset"
                id="cancelBtn"
                variant="outlined"
                color="primary"
                className=""
                onClick={this.handleClearForm}
              >
                Reset
              </Button>
            </div>
          </div>

          <hr />

          <div id="sortingButtons" className="row mb-3">
            <div className="col-5">
              <TextField
                id="search"
                label="Search..."
                margin="normal"
                value={this.state.searchStr}
                onChange={event => this.onInputChange(event, "searchStr")}
                fullWidth={true}
                onKeyPress={this.searchUsers}
              />
            </div>
            <div className="col-1 mt-4">Sort:</div>
            <div className="col-2 mt-3">
              <Button
                id="firstNameSort"
                variant="outlined"
                color="primary"
                className=""
                onClick={() => this.sortByFirstName()}
              >
                First Name
                {currentSortedField === 'firstName' && (firstNameOrder ? <UpArrow/> : <DownArrow/>)}
              </Button>
            </div>
            <div className="col-2 mt-3">
              <Button
                id="lastNameSort"
                variant="outlined"
                color="primary"
                className=""
                onClick={() => this.sortByLastName()}
              >
                Last Name
                {currentSortedField === 'lastName' && (lastNameOrder ? <UpArrow/> : <DownArrow/>)}
              </Button>
            </div>
            <div className="col-2 mt-3">
              <Button
                id="empIdSort"
                variant="outlined"
                color="primary"
                className=""
                onClick={() => this.sortByEmpId()}
              >
                Employee ID
                {currentSortedField === 'empId' && (empIdOrder ? <UpArrow/> : <DownArrow/>)}
              </Button>
            </div>
          </div>

          {this.state.userList.map((list, index) => (
            <div id="userInfoMain" className="card" key={index}>
              <div className="card-body">
                <div className="row">
                  <div className="col-2">
                    <p>Firs Name:</p>
                    <p>Last Name:</p>
                    <p>Employee ID:</p>
                  </div>

                  <div className="col-8">
                    <p>{list.firstName}</p>
                    <p>{list.lastName}</p>
                    <p>{list.empId}</p>
                  </div>

                  <div className="col-2">
                    <Button
                      type="submit"
                      variant="outlined"
                      color="primary"
                      className=""
                      onClick={() => this.setUpdateForm(list)}
                    >
                      Update
                    </Button>
                    <Button
                      type="submit"
                      variant="outlined"
                      color="primary"
                      className=""
                      onClick={() => this.deleteUser(list.id)}
                    >
                      Delete
                    </Button>
                  </div>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>
    );
  }
}