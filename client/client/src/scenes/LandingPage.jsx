import React, { useEffect, useState } from "react";
import { Grid, Box, Stack } from "@mui/material";
import { styled } from "@mui/material/styles";
import LatviaMap from "../components/LatviaMap/LatviaMap";
import Layout from "../components/layout/Layout";
import Graph from "../components/Graph";
import Widget from "../components/Widget";

const LandingPage = () => {
  const [regionStats, setRegionStats] = useState([]);
  const [error, setError] = useState(null);
  const [selectedRegion, setSelectedRegion] = useState(null);

   useEffect(() => {
    const fetchRegionStats = async () => {
      try {
        let link = (`${process.env.REACT_APP_BACKEND}advertisements/stats`) + (selectedRegion?`?city=${selectedRegion}`:"");
        console.log(link);
        const response = await fetch(
          link
        );
        if (!response.ok) throw new Error(`Error: ${response.statusText}`);
        const data = await response.json();
        console.log(data);
        setRegionStats(data);
      } catch (err) {
        setError(err.message);
      }
    };
    fetchRegionStats();
  }, [selectedRegion]);

  const selectedStats = regionStats.find(
    (stat) => stat.regionSlug === selectedRegion
  );  

  return (
    <Layout>
      <Box sx={{ height: "100vh", padding: 2 }}>
        <Grid container sx={{justifyContent: "center"}}>
          
          <Grid size={{ xs: 6, md: 8 }} sx={{alignItems: "stretch"}}>
            <LatviaMap regionStats={regionStats} onRegionClick={setSelectedRegion} />
          </Grid>
          <Grid size={{ xs: 6, md: 4 }} sx={{justifyContent: "flex-start"}}>
            <Stack spacing={2}>
              <Widget
                text={"Vidējā īpašuma cena"}
                number={selectedStats?.avgPrice ?? "N/A"}
                backgroundColor={"#c44536"}
              />
              <Widget
                text={"Sludinājumu skaits"}
                number={selectedStats?.adCount ?? "N/A"}
                backgroundColor={"#c44536"}
              />
              <Widget
                text={"Vidējā cena par kvadrātmetru"}
                number={selectedStats?.avgPricePerSquareMeter ?? "N/A"}
                backgroundColor={"#197278"}
              />
              <Graph />
            </Stack>
          </Grid>
            
        </Grid>
      </Box>
    </Layout>
  );
};

export default LandingPage;
