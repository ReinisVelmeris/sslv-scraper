import React from 'react';
import { Link } from 'react-router-dom';
import './NavBar.css';
import { Typography } from '@mui/material';

const NavBar = () => {
  return (
    <nav className="navbar">
      <div className="navbar-logo"><Typography>AAAAAAA</Typography></div>
      <div className="navbar-links"><Typography>
        <Link to="/">home</Link>
        <Link to="/stats">stats</Link>
        <Link to="/about">about</Link></Typography>
      </div>
    </nav>
  );
};

export default NavBar;