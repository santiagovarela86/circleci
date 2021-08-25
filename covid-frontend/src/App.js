import React, { useEffect } from 'react';
import './assets/css/App.css';
import {
  BrowserRouter as Router,
  Switch,
  Route,
  useHistory,
} from 'react-router-dom';
import Home from './components/home/home';
import Login from './components/login/login';
import Register from './components/register/Register';
import Recovery from './components/recovery/Recovery';
import RestProvider from './rest/RestProvider';
import User from './models/User'
import 'react-toastify/dist/ReactToastify.css';
import { ToastContainer } from 'react-toastify';

// eslint-disable-next-line require-jsdoc
function App() {
  const [userLogged, setUserLogged] = React.useState(new User());

  return (
    <div>
      <ToastContainer position="top-right" hideProgressBar={true}/>
      <Router>
        {/* A <Switch> looks through its children <Route>s and
            renders the first one that matches the current URL. */}
        <Switch>
          <Route path="/recovery">
            <Recovery />
          </Route>
          <Route path="/register">
            <Register />
          </Route>
          <Route path="/login">
            <Login setUserLogged={setUserLogged} />
          </Route>
          <Route path="/logout">
            <Logout />
          </Route>
          <Route path="/home">
            <Home userLogged={userLogged} path="Listas" setUserLogged={setUserLogged} />
          </Route>
          <Route path="/users">
            <Home userLogged={userLogged} path="Usuarios" setUserLogged={setUserLogged} />
          </Route>
          <Route path="/compare">
            <Home userLogged={userLogged} path="Comparar Listas" setUserLogged={setUserLogged} />
          </Route>
          <Route path="/myuser">
            <Home userLogged={userLogged} path="Mi Perfil" setUserLogged={setUserLogged} />
          </Route>
          <Route path="/plot">
            <Home userLogged={userLogged} path="Ploteos" setUserLogged={setUserLogged} />
          </Route>
          <Route path="/compare-strategies">
            <Home userLogged={userLogged} path="Comparar Estrategias" setUserLogged={setUserLogged} />
          </Route>
          <Route path="/countryinterest">
            <Home userLogged={userLogged} path="Interés Países" setUserLogged={setUserLogged} />
          </Route>
          <Route path="/countrylistreport">
            <Home userLogged={userLogged} path="Reporte Listas" setUserLogged={setUserLogged} />
          </Route>
          <Route path="/paises">
            <Home userLogged={userLogged} path="paises" setUserLogged={setUserLogged} />
          </Route>
          <Route path="/">
            <Login userLogged={userLogged} setUserLogged={setUserLogged} ></Login>
          </Route>
        </Switch>
      </Router>
    </div>
  );
}

// eslint-disable-next-line require-jsdoc
function Logout() {
  const history = useHistory();

  useEffect(() => {
    localStorage.removeItem('token');
    RestProvider.token = null;
    history.push('/login');
  }, [history]);

  return (
    <div />
  );
}

export default App;
