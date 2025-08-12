document.addEventListener("DOMContentLoaded", () => {
  const customerForm = document.getElementById("customerForm");
  const customerIdInput = document.getElementById("customerId");
  const datetimeInput = document.getElementById("datetime"); // नया इनपुट
  const operatorNameInput = document.getElementById("operatorName"); // नया इनपुट
  const customerNameInput = document.getElementById("customerName");
  const fatherNameInput = document.getElementById("fatherName");
  const spouseNameInput = document.getElementById("spouseName");
  const mobileNumberInput = document.getElementById("mobileNumber");
  const workLogSelect = $("#workLogSelect");
  const workDescriptionInput = document.getElementById("workDescription");
  const costingInput = document.getElementById("costing");
  const callAmountInput = document.getElementById("callAmount");
  const depositAmountInput = document.getElementById("depositAmount");
  const discountOrLossInput = document.getElementById("discountOrLoss");
  const dueAmountInput = document.getElementById("dueAmount");
  const tokenNumberInput = document.getElementById("tokenNumber");
  const emitraIdInput = document.getElementById("emitraId");
  const workStatusInput = document.getElementById("workStatus");
  const otherRemarkInput = document.getElementById("otherRemark");

  const customerTableBody = document.getElementById("customerTableBody");
  const formTitle = document.getElementById("formTitle");
  const submitButton = document.getElementById("submitButton");
  const cancelButton = document.getElementById("cancelButton");

  let workLogsData = [];

  workLogSelect.select2({
    placeholder: "Select work logs",
    allowClear: true,
  });

  const setInitialFormValues = () => {
    // 12 घंटे के प्रारूप में वर्तमान समय सेट करें
    const now = new Date();
    const formattedDateTime = now.toLocaleString("en-US", {
      year: "numeric",
      month: "2-digit",
      day: "2-digit",
      hour: "2-digit",
      minute: "2-digit",
      second: "2-digit",
      hour12: true,
    });
    datetimeInput.value = formattedDateTime;

    // ऑपरेटर का नाम सेट करें (यह आपके लॉगिन सेशन से आएगा)
    // फिलहाल, हम इसे एक स्थिर मान दे रहे हैं
    operatorNameInput.value = "Admin";
  };

  const fetchWorkLogs = async () => {
    try {
      const response = await axios.get("/api/worklogs");
      workLogsData = response.data;
      workLogSelect.empty();
      workLogsData.forEach((worklog) => {
        const option = new Option(worklog.workName, worklog.id);
        workLogSelect.append(option);
      });
      workLogSelect.trigger("change");
    } catch (error) {
      console.error("Error fetching work logs:", error);
    }
  };

  const calculateDueAmount = () => {
    const callAmount = parseFloat(callAmountInput.value) || 0;
    const depositAmount = parseFloat(depositAmountInput.value) || 0;
    const discount = parseFloat(discountOrLossInput.value) || 0;

    const dueAmount = callAmount - depositAmount - discount;
    dueAmountInput.value = dueAmount.toFixed(2);
  };

  const calculateCosting = () => {
    const selectedWorklogIds = workLogSelect.val();
    let totalCosting = 0;
    if (selectedWorklogIds) {
      selectedWorklogIds.forEach((id) => {
        const worklog = workLogsData.find((wl) => wl.id === parseInt(id));
        if (worklog) {
          totalCosting += worklog.customerCost;
        }
      });
    }
    costingInput.value = totalCosting.toFixed(2);
  };

  callAmountInput.addEventListener("input", calculateDueAmount);
  depositAmountInput.addEventListener("input", calculateDueAmount);
  discountOrLossInput.addEventListener("input", calculateDueAmount);
  workLogSelect.on("change", calculateCosting);

  const fetchCustomers = async () => {
    try {
      const response = await axios.get("/api/customers");
      const customers = response.data;
      renderCustomers(customers);
    } catch (error) {
      console.error("Error fetching customers:", error);
      alert("Failed to load customers.");
    }
  };

  const renderCustomers = (customers) => {
    customerTableBody.innerHTML = "";
    customers.forEach((customer) => {
      const row = document.createElement("tr");
      row.innerHTML = `
                <td>${
                  customer.datetime
                    ? new Date(customer.datetime).toLocaleString("en-US", {
                        year: "numeric",
                        month: "2-digit",
                        day: "2-digit",
                        hour: "2-digit",
                        minute: "2-digit",
                        second: "2-digit",
                        hour12: true,
                      })
                    : ""
                }</td>
                <td>${customer.customerName}</td>
                <td>${customer.mobileNumber || ""}</td>
                <td>${customer.callAmount || "0.00"}</td>
                <td>${customer.dueAmount || "0.00"}</td>
                <td>${customer.operatorName}</td>
                <td>${customer.workStatus || ""}</td>
                <td>
                    <button class="btn btn-sm btn-info edit-btn" data-id="${
                      customer.id
                    }">Edit</button>
                    <button class="btn btn-sm btn-danger delete-btn" data-id="${
                      customer.id
                    }">Delete</button>
                </td>
            `;
      customerTableBody.appendChild(row);
    });
  };

  customerForm.addEventListener("submit", async (e) => {
    e.preventDefault();

    const customerData = {
      customerName: customerNameInput.value,
      fatherName: fatherNameInput.value,
      spouseName: spouseNameInput.value,
      mobileNumber: mobileNumberInput.value,
      workDescription: workDescriptionInput.value,
      costing: parseFloat(costingInput.value),
      // अब dueAmount को यहां से हटा दें
      callAmount: parseFloat(callAmountInput.value),
      depositAmount: parseFloat(depositAmountInput.value),
      discountOrLoss: parseFloat(discountOrLossInput.value) || 0,
      tokenNumber: tokenNumberInput.value,
      emitraId: emitraIdInput.value,
      workStatus: workStatusInput.value,
      otherRemark: otherRemarkInput.value,
      typeOfWork: workLogSelect.val().map((id) => ({ id: parseInt(id) })),
    };

    // Debugging के लिए: कंसोल में डेटा प्रिंट करें
    console.log("Sending data to backend:", customerData);

    const customerId = customerIdInput.value;
    const method = customerId ? "put" : "post";
    const url = customerId ? `/api/customers/${customerId}` : "/api/customers";

    try {
      await axios[method](url, customerData);
      customerForm.reset();
      customerIdInput.value = "";
      formTitle.textContent = "Add New Customer";
      submitButton.textContent = "Add Customer";
      workLogSelect.val(null).trigger("change");
      setInitialFormValues(); // फ़ॉर्म को रीसेट करने के बाद प्रारंभिक मान सेट करें
      calculateDueAmount();
      calculateCosting();
      fetchCustomers();
      alert("Customer saved successfully!");
    } catch (error) {
      console.error("Error saving customer:", error);
      alert("Failed to save customer.");
    }
  });

  customerTableBody.addEventListener("click", (e) => {
    if (e.target.classList.contains("edit-btn")) {
      const customerId = e.target.dataset.id;

      axios.get(`/api/customers/${customerId}`).then((response) => {
        const customer = response.data;
        customerIdInput.value = customer.id;
        datetimeInput.value = customer.datetime
          ? new Date(customer.datetime).toLocaleString("en-US", {
              year: "numeric",
              month: "2-digit",
              day: "2-digit",
              hour: "2-digit",
              minute: "2-digit",
              second: "2-digit",
              hour12: true,
            })
          : "";
        operatorNameInput.value = customer.operatorName || "Admin";
        customerNameInput.value = customer.customerName;
        fatherNameInput.value = customer.fatherName || "";
        spouseNameInput.value = customer.spouseName || "";
        mobileNumberInput.value = customer.mobileNumber || "";
        workDescriptionInput.value = customer.workDescription || "";
        callAmountInput.value = customer.callAmount || "0.00";
        depositAmountInput.value = customer.depositAmount || "0.00";
        discountOrLossInput.value = customer.discountOrLoss || "0.00";
        dueAmountInput.value = customer.dueAmount || "0.00";
        tokenNumberInput.value = customer.tokenNumber || "";
        emitraIdInput.value = customer.emitraId || "";
        workStatusInput.value = customer.workStatus || "";
        otherRemarkInput.value = customer.otherRemark || "";

        if (customer.typeOfWork) {
          const worklogIds = customer.typeOfWork.map((wl) => wl.id.toString());
          workLogSelect.val(worklogIds).trigger("change");
        } else {
          workLogSelect.val(null).trigger("change");
        }
        calculateCosting();
        calculateDueAmount();
      });

      formTitle.textContent = "Edit Customer";
      submitButton.textContent = "Update Customer";
    }

    if (e.target.classList.contains("delete-btn")) {
      const customerId = e.target.dataset.id;
      if (confirm("Are you sure you want to delete this customer?")) {
        deleteCustomer(customerId);
      }
    }
  });

  const deleteCustomer = async (id) => {
    try {
      await axios.delete(`/api/customers/${id}`);
      fetchCustomers();
      alert("Customer deleted successfully!");
    } catch (error) {
      console.error("Error deleting customer:", error);
      alert("Failed to delete customer.");
    }
  };

  cancelButton.addEventListener("click", () => {
    customerForm.reset();
    customerIdInput.value = "";
    formTitle.textContent = "Add New Customer";
    submitButton.textContent = "Add Customer";
    workLogSelect.val(null).trigger("change");
    setInitialFormValues();
    calculateDueAmount();
    calculateCosting();
  });

  // पेज लोड होने पर फ़ॉर्म को इनिशियलाइज़ करें
  setInitialFormValues();
  fetchCustomers();
  fetchWorkLogs();
});
