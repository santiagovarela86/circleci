import React,{useState} from 'react';
import Avatar from '@material-ui/core/Avatar';
import Button from '@material-ui/core/Button';
import CssBaseline from '@material-ui/core/CssBaseline';
import TextField from '@material-ui/core/TextField';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Checkbox from '@material-ui/core/Checkbox';
import { Link } from 'react-router-dom';
import MaterialLink from '@material-ui/core/Link';
import Box from '@material-ui/core/Box';
import LockOutlinedIcon from '@material-ui/icons/LockOutlined';
import Typography from '@material-ui/core/Typography';
import { makeStyles } from '@material-ui/core/styles';
import Container from '@material-ui/core/Container';
import Grid from '@material-ui/core/Grid';
import RestProvider from '../../rest/RestProvider';
import { useHistory } from "react-router-dom";
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

export default function SignIn() {
  const classes = useStyles();
  const [errorUserNAme, setErrorUserNAme] = useState(false);
  const [errorPassword, setErrorPassword] = useState(false);
  const [errorName, setErrorNAme] = useState(false);
  const [errorLastName, setErroLastName] =  useState(false);
  const [errorEmail, setErrorEmail] = useState(false);
  const [errorConfirPass,setErrorConfirPass] = useState(false);
  var history = useHistory();

  const [userInput, setUserInput] = useState({
        username:'',
        name:"",
        lastname:'',
        email:"",
        password:"",
        confirpassword:"",
  })

  const handleInputChange = (event)=>{
    const {name,value } = event.target

    setUserInput({
      ...userInput ,
      [name]:value
    })
    switch (name) {
      case "username":
        setErrorUserNAme(value === "");
        break;
      case "email":
        setErrorEmail(value==="");
        break;
      case "password":
        setErrorPassword(value === "");
        break;
      case "name":
        setErrorNAme(value==="");
        break;
      case "lastname":
        setErroLastName(value ==="");
        break;
      case "confirpassword":
        setErrorConfirPass(value==="");
        break;
      default:
        break;
    }

  }

  const handleSubmit = (event)=>{
    event.preventDefault();
    console.log(event);
    if (userInput.username === "") {
      setErrorUserNAme(true);
        return;
    }
    if (userInput.name === "") {
      setErrorNAme(true);
      return;
    }
    if (userInput.lastname === "") {
      setErroLastName(true);
      return;
    }
    if (userInput.email === "") {
      setErrorEmail(true);
      return;
    }
    if (userInput.password === "") {
      setErrorPassword(true);
      return;
    }
    if (userInput.confirpassword === "") {
      setErrorConfirPass(true);
      return;
    }
    if(userInput.password !== userInput.confirpassword ){
        toast.error('Las contraseñas no coinciden');
        return; 
    }
    submit();
  }

  function submit (){
    new RestProvider().register({ user_name: userInput.username, password: userInput.password,name:userInput.name,last_name:userInput.lastname,
									email:userInput.email})
      .then(res => {
        toast.success("Se registró correctamente"); 
        history.push("/");
      })
      .catch(err => {
        if (err.status === 400){
          toast.error("Usuario o Mail ya registrado. Pruebe con otro.");
        }else{
          toast.error("Hubo un error de conexión, inténtelo de nuevo más tarde ")
        }
      });
  }
  return (
    <Container component="main" maxWidth="xs">
      <CssBaseline />
      <div className={classes.paper}>
        <Avatar className={classes.avatar}>
          <LockOutlinedIcon />
        </Avatar>
        <Typography component="h1" variant="h5">
          Crear cuenta
        </Typography>
        <form className={classes.form} onSubmit={handleSubmit} noValidate>
        <TextField
            error ={errorUserNAme}
            variant="outlined"
            margin="normal"
            required
            fullWidth
            id="username"
            label="usuario"
            name="username"
            onChange={handleInputChange}
            helperText="Campo obligatorio"
            autoFocus
            
        />
        <TextField
            error = {errorName}
            variant="outlined"
            margin="normal"
            required
            fullWidth
            id="name"
            label="nombre"
            name="name"
            onChange={handleInputChange}
            helperText="Campo obligatorio"
            autoFocus
          />
          <TextField
            error = {errorLastName}
            variant="outlined"
            margin="normal"
            required
            fullWidth
            id="lastname"
            label="apellido"
            name="lastname"
            onChange={handleInputChange}
            helperText="Campo obligatorio"
            autoFocus
          />
          
          <TextField
            error = {errorEmail}
            variant="outlined"
            margin="normal"
            required
            fullWidth
            id="email"
            label="Direccion de email"
            name="email"
            autoComplete="email"
            onChange={handleInputChange}
            helperText="Campo obligatorio"
            autoFocus
          />
          <TextField
            error = {errorPassword}
            variant="outlined"
            margin="normal"
            required
            fullWidth
            name="password"
            label="contraseña"
            type="password"
            id="password"
            onChange={handleInputChange}
            helperText="Campo obligatorio"
            autoComplete="current-password"
          />
          <TextField
            error = {errorConfirPass}
            variant="outlined"
            margin="normal"
            required
            fullWidth
            name="confirpassword"
            label="confirmar contraseña"
            type="password"
            id="confirpassword"
            onChange={handleInputChange}
            helperText="Campo obligatorio"
            autoComplete="current-password"
          />
          <FormControlLabel
            control={<Checkbox value="remember" color="primary" />}
            label="Recordarme"
          />
          <Button
            type="submit"
            fullWidth
            variant="contained"
            color="primary"
            className={classes.submit}
          >
            Registrar
          </Button>
          <Grid container>
            <Grid item xs>
              <Link to="/login" variant="body2">
                ¿Ya tienes cuenta? Iniciar sesión
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