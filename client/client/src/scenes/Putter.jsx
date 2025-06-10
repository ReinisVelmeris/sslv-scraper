import { useEffect } from "react";

const Putter = () => {
  useEffect(() => {
    // Inject the script dynamically after component mounts
    const script = document.createElement("script");
    script.src = "https://js.puter.com/v2/";
    script.async = true;
    script.onload = () => {
        if (window.puter) {
            window.puter.ai.chat('')
              .then(response => {
                window.puter.print(response);
              });
          }
    };

    document.body.appendChild(script);

    return () => {
      document.body.removeChild(script); // clean up on unmount
    };
  }, []);

  return (
    <div>
      <h2>Puter AI Chat Output:</h2>
      {/* The script likely prints directly to document or DOM */}
    </div>
  );
};

export default Putter;
