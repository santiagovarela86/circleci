import React, { useEffect } from 'react';
import clsx from 'clsx';
import { makeStyles, useTheme } from '@material-ui/core/styles';
import Drawer from '@material-ui/core/Drawer';
import CssBaseline from '@material-ui/core/CssBaseline';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import List from '@material-ui/core/List';
import Divider from '@material-ui/core/Divider';
import IconButton from '@material-ui/core/IconButton';
import MenuIcon from '@material-ui/icons/Menu';
import ChevronLeftIcon from '@material-ui/icons/ChevronLeft';
import ChevronRightIcon from '@material-ui/icons/ChevronRight';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import ListIcon from '@material-ui/icons/List'
import MapIcon from '@material-ui/icons/Map'
import AccountIcon from '@material-ui/icons/AccountCircle'
import CompareIcon from '@material-ui/icons/Compare'
import ShowChartIcon from '@material-ui/icons/ShowChart'
import PowerSettingsNew from '@material-ui/icons/PowerSettingsNew'
import AccountCircle from '@material-ui/icons/AccountCircle'
import InsertChartOutlinedIcon from '@material-ui/icons/InsertChartOutlined'
import InsertChartIcon from '@material-ui/icons/InsertChart'
import ListsGrid from './components/listGrid/listsGrid';
import Plot from './components/plot/plot';
import CompareStrategies from './components/compareStrategies/compareStrategies';
import MyUser from './components/myUser/myUser';
import UsersGrid from './components/usersGrid/usersGrid';
import User from '../../models/User';
import { useHistory } from "react-router-dom";
import RestProvider from '../../rest/RestProvider';
import CountryList from '../../models/CountryList';
import { toast } from 'react-toastify';
import Country from "../../models/Country";
import CompareListsGrid from "./components/compareListsGrid/compareListsGrid"
import CountryInterestGrid from './components/countryInterestGrid/countryInterestGrid';
import ListReport from './components/listReport/listReport';
import CountriesListTag from './components/CountryListTag/CountryListTag'
import PublicRoundedIcon from '@material-ui/icons/PublicRounded';
const drawerWidth = 300;
const useStyles = makeStyles((theme) => ({
    root: {
        display: 'flex',
    },
    appBar: {
        transition: theme.transitions.create(['margin', 'width'], {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.leavingScreen,
        }),
    },
    appBarShift: {
        width: `calc(100% - ${drawerWidth}px)`,
        marginLeft: drawerWidth,
        transition: theme.transitions.create(['margin', 'width'], {
            easing: theme.transitions.easing.easeOut,
            duration: theme.transitions.duration.enteringScreen,
        }),
    },
    menuButton: {
        marginRight: theme.spacing(2),
    },
    hide: {
        display: 'none',
    },
    drawer: {
        width: drawerWidth,
        flexShrink: 0,
    },
    drawerPaper: {
        width: drawerWidth,
    },
    drawerHeader: {
        display: 'flex',
        alignItems: 'center',
        padding: theme.spacing(0, 1),
        // necessary for content to be below app bar
        ...theme.mixins.toolbar,
        justifyContent: 'flex-end',
    },
    content: {
        flexGrow: 1,
        padding: theme.spacing(3),
        transition: theme.transitions.create('margin', {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.leavingScreen,
        }),
        marginLeft: -drawerWidth,
    },
    contentShift: {
        transition: theme.transitions.create('margin', {
            easing: theme.transitions.easing.easeOut,
            duration: theme.transitions.duration.enteringScreen,
        }),
        marginLeft: 0,
    },
}));

export default function Home(props: { path: string, userLogged: User, setUserLogged: (user: User) => void }) {
    const classes = useStyles();
    const theme = useTheme();
    const [open, setOpen] = React.useState(true);
    const [selectedList, setSelectedList] = React.useState(new CountryList());
    const [isAdmin, setIsAdmin] = React.useState(false);

    var history = useHistory();

    useEffect(() => {
        new RestProvider().isAdmin({})
            .then(() => {
                setIsAdmin(true)
            })
    }, [])

    useEffect(() => {
        if (props.userLogged.id === 0) {
            var token = localStorage.getItem("token");
            if (token) {
                RestProvider.token = token;
                new RestProvider().getUser({})
                    .then(user => {
                        props.setUserLogged(user)
                    })
                    .catch(err => {
                        if (err.status === 401) {
                            toast.error("La sesión no es válida. Inicie sesión nuevamente");
                            RestProvider.token = "";
                            history.push("/login");
                        }
                    })
            } else {
                toast.error("La sesión no es válida. Inicie sesión nuevamente");
                history.push("/login");
            }
        }
    }, [history, props])

    const handleDrawerOpen = () => {
        setOpen(true);
    };

    const handleDrawerClose = () => {
        setOpen(false);
    };

    const logout = () => {
        history.push("/logout");
    }

    const goToPlot = (countryList: CountryList) => {
        setSelectedList(countryList);
        history.push("/plot");
    }

    const goToUsers = () => {
        new RestProvider().isAdmin({})
            .then(() => {
                history.push("/users");
            })
            .catch(err => toast.error("Debe ser Administrador para ver esa información"));
    }

    const goToCompare = () => {
        new RestProvider().isAdmin({})
            .then(() => {
                history.push("/compare");
            })
            .catch(err => toast.error("Debe ser Administrador para ver esa información"));
    }

    const goToInterest = () => {
        new RestProvider().isAdmin({})
            .then(() => {
                history.push("/countryinterest");
            })
            .catch(err => toast.error("Debe ser Administrador para ver esa información"));
    }

    const goToCountryListReport = () => {
        new RestProvider().isAdmin({})
            .then(() => {
                history.push("/countrylistreport");
            })
            .catch(err => toast.error("Debe ser Administrador para ver esa información"));
    }
    const goToCountryListTag = () => {
        new RestProvider().isAdmin({})
            .then(() => {
                history.push("/paises");
            })
            .catch(err => toast.error("Debe ser Administrador para ver esa información"));
    }

    return (
        <div className={classes.root}>
            <CssBaseline />
            <AppBar
                position="fixed"
                className={clsx(classes.appBar, {
                    [classes.appBarShift]: open,
                })}
            >
                <Toolbar>
                    <IconButton
                        color="inherit"
                        aria-label="open drawer"
                        onClick={handleDrawerOpen}
                        edge="start"
                        className={clsx(classes.menuButton, open && classes.hide)}
                    >
                        <MenuIcon />
                    </IconButton>
                </Toolbar>
            </AppBar>
            <Drawer
                className={classes.drawer}
                variant="persistent"
                anchor="left"
                open={open}
                classes={{
                    paper: classes.drawerPaper,
                }}
            >
                <div className={classes.drawerHeader}>
                    <IconButton onClick={handleDrawerClose}>
                        {theme.direction === 'ltr' ? <ChevronLeftIcon /> : <ChevronRightIcon />}
                    </IconButton>
                </div>
                <Divider />
                <List style={{ height: "100%", display: "flex", flexDirection: "column" }}>
                    <ListItem button onClick={() => history.push("/home")}>
                        <ListItemIcon><ListIcon /></ListItemIcon>
                        <ListItemText primary="Mis Listas" />
                    </ListItem>
                    <ListItem button onClick={() => history.push("/plot")}>
                        <ListItemIcon><InsertChartOutlinedIcon /></ListItemIcon>
                        <ListItemText primary="Ploteos" />
                    </ListItem>
                    <ListItem button onClick={() => history.push("/compare-strategies")}>
                        <ListItemIcon><InsertChartIcon /></ListItemIcon>
                        <ListItemText primary="Comparar Estrategias" />
                    </ListItem>
                    {
                        isAdmin &&
                        <div>
                            <ListItem button onClick={goToUsers}>
                                <ListItemIcon><AccountIcon /></ListItemIcon>
                                <ListItemText primary="Usuarios" />
                            </ListItem>
                            <ListItem button onClick={goToCompare}>
                                <ListItemIcon><CompareIcon /></ListItemIcon>
                                <ListItemText primary="Comparar Listas" />
                            </ListItem>
                            <ListItem button onClick={goToInterest}>
                                <ListItemIcon><MapIcon /></ListItemIcon>
                                <ListItemText primary="Interés por País" />
                            </ListItem>
                            <ListItem button onClick={goToCountryListReport}>
                                <ListItemIcon><ShowChartIcon /></ListItemIcon>
                                <ListItemText primary="Reporte de Listas" />
                            </ListItem>
                            <ListItem button onClick={goToCountryListTag}>
                                <ListItemIcon><PublicRoundedIcon /></ListItemIcon>
                                <ListItemText primary="Paises" />
                            </ListItem>
                        </div>
                    }
                    <ListItem button onClick={() => history.push("/myuser")}
                        style={{ marginTop: "auto" }}>
                        <ListItemIcon><AccountCircle /></ListItemIcon>
                        <ListItemText primary={props.userLogged.name + ' ' + props.userLogged.last_name} />
                    </ListItem>
                    <ListItem button onClick={logout}>
                        <ListItemIcon><PowerSettingsNew style={{ fill: "#f44336" }} /></ListItemIcon>
                        <ListItemText primary="Logout" />
                    </ListItem>
                </List>
            </Drawer>
            <main
                className={clsx(classes.content, {
                    [classes.contentShift]: open,
                })}
            >
                <div className={classes.drawerHeader} />
                {props.path === "Listas" &&
                    <ListsGrid userLogged={props.userLogged} rows={CountryList} goToPlot={goToPlot} />
                }
                {props.path === "Ploteos" &&
                    <Plot userLogged={props.userLogged} selectedList={selectedList} changeSelectedList={setSelectedList} drawerOpen={open} />
                }
                {props.path === "Comparar Estrategias" &&
                    <CompareStrategies drawerOpen={open} />
                }
                {props.path === "Usuarios" &&
                    <UsersGrid userLogged={props.userLogged} rows={User} />
                }
                {props.path === "Mi Perfil" &&
                    <MyUser userLogged={props.userLogged} />
                }
                {props.path === "Comparar Listas" &&
                    <CompareListsGrid />
                }
                {props.path === "Interés Países" &&
                    <CountryInterestGrid userLogged={props.userLogged} rows={Country} />
                }
                {props.path === "Reporte Listas" &&
                    <ListReport />
                }
                {props.path === "paises" &&
                    <CountriesListTag userLogged={props.userLogged} rows={Country} />
                }
            </main>
        </div>
    )
}