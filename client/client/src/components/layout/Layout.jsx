import React from 'react'
import NavBar from '../navbar/NavBar';
import './Layout.css';

const Layout = ({children}) => {
  return (
    <>
    <NavBar />
    <main
         style={{
            backgroundImage: 'url("/background.png")',
            backgroundSize: 'cover',
            backgroundRepeat: 'no-repeat',
            backgroundPosition: 'center',
            // width: '100vw',
            height: '100vh',
            margin: 0,
          }}
      >
        {children}
      </main>
  </>
  )
}

export default Layout