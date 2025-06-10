import { Box, Stack, Typography } from "@mui/material";
import React, {useState, useEffect} from "react";

const Widget = ({text, number, backgroundColor}) => {
  const [displayNumber, setDisplayNumber] = useState(0);

  //skaitļošanas efekts
  // useEffect( () => {
  //   let start = 0;
  //   const duration = 900;
  //   const steps = 15;
  //   const stepTime = duration / steps;
  //   const increment = (number - start) / steps;

  //   const interval  = setInterval(() => {
  //     start += increment;
  //     if (start >= number) {
  //       clearInterval(interval);
  //       setDisplayNumber(number);
  //     }else{
  //       setDisplayNumber(Math.round(start))
  //     }
  //   }, stepTime);
  //   return () => clearInterval();
  // }, [number]);
  
  return <Box sx={{ width: "50%", height: "100%", bgcolor: {backgroundColor}, color: "#ffffff", padding: "20px", borderRadius: "8px" }}>
    <Stack>
      <Typography>
        {text}
      </Typography>
      <Typography sx={{fontSize: "3vh", fontWeight: "bold"}}>
        {number}
      </Typography>
    </Stack>
  </Box>;
};

export default Widget;
