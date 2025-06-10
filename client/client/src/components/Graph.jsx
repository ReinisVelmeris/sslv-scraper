import React, { useEffect, useState } from 'react'
import { LineChart } from '@mui/x-charts'

const Graph = () => {
  const [graphData, setGraphData] = useState([]);
  const [error, setError] = useState(null);
  const [currentYear, setCurrentYear] = useState("");
  const [currentMonth, setCurrentMonth] = useState("");
  const [currentCity, setCurrentCity] = useState("");
  const [currentDistrict, setCurrentDistrict] = useState("");

  useEffect(() => {
    const fetchStats = async () => {
      try {
        let query = '?' + new URLSearchParams(
          Object.entries({
            year: currentYear,
            month: currentMonth,
            city: currentCity,
            district: currentDistrict
          }).filter(([_, v]) => v != null)
        ).toString();

        let link = `${process.env.REACT_APP_BACKEND}advertisements/stats/graph` + query;
        const response = await fetch(link);
        if (!response.ok) {
          throw new Error('Failed to fetch graph data');
        }
        const data = await response.json();
        setGraphData(data);
        console.log(data);
      } catch (err) {
        setError(err.message);
      }
    };

    fetchStats();
  }, [currentCity]);


  const dataset = [
    { date: new Date(1990, 0, 1), fr: 2, dl: 3, gb: 2.5 },
    { date: new Date(1995, 0, 1), fr: 3, dl: 4, gb: 3.2 },
    { date: new Date(2000, 0, 1), fr: 4.5, dl: 5.2, gb: 4.8 },
    { date: new Date(2005, 0, 1), fr: 5.5, dl: 6.2, gb: 5.7 },
    { date: new Date(2010, 0, 1), fr: 6.2, dl: 7, gb: 6.5 },
    { date: new Date(2015, 0, 1), fr: 7.1, dl: 8.1, gb: 7.2 },
  ];
  
  return (
    <LineChart
        dataset={graphData}
        xAxis={[
          {
            id: 'Years',
            dataKey: 'year',
            scaleType: 'band', // categorical axis
            data: ['2020', '2021', '2022', '2023', '2024', '2025'],
            valueFormatter: (year) => year,
          },
        ]}
        yAxis={[
          {
            width: 70,
          },
        ]}
        series={[
          {
            id: 'France',
            label: 'average sale price',
            dataKey: 'avgSalePrice',
            stack: 'total',
            showMark: false,
          },
          {
            id: 'Germany',
            label: 'Average rent price',
            dataKey: 'avgRentPrice',
            stack: 'total',
            showMark: false,
          }
        ]}
        height={400}
      />
  )
}

export default Graph