import React, { useState, useEffect } from 'react';
import Avatar from '@material-ui/core/Avatar';
import Button from '@material-ui/core/Button';
import CssBaseline from '@material-ui/core/CssBaseline';
import TextField from '@material-ui/core/TextField';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Checkbox from '@material-ui/core/Checkbox';
import { Link } from 'react-router-dom';
import MaterialLink from '@material-ui/core/Link';
import Grid from '@material-ui/core/Grid';
import Box from '@material-ui/core/Box';
import LockOutlinedIcon from '@material-ui/icons/LockOutlined';
import Typography from '@material-ui/core/Typography';
import { makeStyles } from '@material-ui/core/styles';
import Container from '@material-ui/core/Container';
import { useHistory } from "react-router-dom";
import RestProvider from '../../rest/RestProvider';
import { toast } from 'react-toastify';

function Copyright() {
  return (
    <Typography variant="body2" color="textSecondary" align="center">
      {'Copyright © '}
      <MaterialLink color="inherit" href="http://www.tacs-utn.com.ar/">
        Your Website
      </MaterialLink>{' '}
      {new Date().getFullYear()}
      {'.'}
    </Typography>
  );
}

const useStyles = makeStyles((theme) => ({
  paper: {
    marginTop: theme.spacing(8),
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
  },
  avatar: {
    margin: theme.spacing(1),
    backgroundColor: theme.palette.secondary.main,
  },
  form: {
    width: '100%', // Fix IE 11 issue.
    marginTop: theme.spacing(1),
  },
  submit: {
    margin: theme.spacing(3, 0, 2),
  },
}));

export default function Login(props) {
  const classes = useStyles();

  const [userData, setUserData] = useState({
    username: "",
    password: ""
  })
  const [remember, setRemember] = useState(true);

  const [errorUser, setErrorUser] = useState(false);
  const [errorPassword, setErrorPassword] = useState(false);
  var history = useHistory();

  useEffect(() => {
    var token = localStorage.getItem("token");
    if (token) {
      RestProvider.token = token;
      new RestProvider().getUser()
      .then(user => {
        props.setUserLogged(user)
        history.push("/home");
      })
    }
  }, [history, props])

  const handleInputChange = event => {
    const { name, value } = event.target;

    setUserData({
      ...userData,
      [name]: value
    });
    switch (name) {
      case "username":
        setErrorUser(value === "");
        break;
      case "password":
        setErrorPassword(value === "");
        break;
      default:
        break;
    }
  };

  const handleSubmit = event => {
    event.preventDefault();
    if (userData.username === "") {
      setErrorUser(true);
    }
    if (userData.password === "") {
      setErrorPassword(true);
    }
    if (!errorUser && !errorPassword) {
      submit();
    }
  };

  function submit() {
    new RestProvider().login({ user_name: userData.username, password: userData.password })
      .then(res => {
        RestProvider.token = res.token;
        new RestProvider().getUser()
          .then(user => {
            props.setUserLogged(user)
          })

        if (remember) {
          localStorage.setItem("token", res.token);
        }
        history.push("/home");
      })
      .catch(err => {
        if (err.status === 401) {
          toast.error("El usuario o la  contraseña no existen en el sistema");
        } else {
          toast.error("Hubo un error de conexión, inténtelo de nuevo más tarde ");
        }
      })
  }

  return (

    <Container component="main" maxWidth="xs">
      <CssBaseline />
      <div className={classes.paper}>
        <Avatar className={classes.avatar}>
          <LockOutlinedIcon />
        </Avatar>
        <Typography component="h1" variant="h5">
          Ingresar al sistema
        </Typography>
        <form className={classes.form} onSubmit={handleSubmit} noValidate>
          <TextField
            variant="outlined"
            margin="normal"
            required
            fullWidth
            id="username"
            label="usuario"
            name="username"
            onChange={handleInputChange}
            autoFocus
            error={errorUser}
            helperText="Campo obligatorio"
          />
          <TextField
            variant="outlined"
            margin="normal"
            required
            fullWidth
            name="password"
            label="contraseña"
            type="password"
            id="password"
            onChange={handleInputChange}
            autoComplete="current-password"
            error={errorPassword}
            helperText="Campo obligatorio"
          />
          <FormControlLabel
            label="Recordarme"
            control={<Checkbox checked={remember} onChange={(event) => setRemember(event.target.checked)} color="primary" />}
          />
          <Button
            type="submit"
            fullWidth
            variant="contained"
            color="primary"
            className={classes.submit}
          >
            Ingresar
          </Button>
          <Grid container>
            <Grid item xs>
              <Link to="/recovery" variant="body2">
                ¿Olvidaste la contraseña?
              </Link>
            </Grid>
            <Grid item>
              <Link to="/Register" variant="body2">
                {"¿No tenes cuenta? Registrarte"}
              </Link>
            </Grid>
          </Grid>
        </form>
      </div>
      <Box mt={8}>
        <Copyright />
      </Box>
    </Container>
  );
}