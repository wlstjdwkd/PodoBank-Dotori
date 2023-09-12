import React, { useState } from "react";
import {
  View,
  Text,
  StyleSheet,
  TouchableOpacity,
  Dimensions,
} from "react-native";
import { Ionicons } from "@expo/vector-icons";

import HeaderScreen from "../Header/HeaderScreen";

const { width } = Dimensions.get("window");

export default function TransferAmountScreen({ route, navigation }) {
  const [amount, setAmount] = useState(0);
  const accountBalance = 10000; // 이 값은 실제 계좌 잔액에 따라 바뀌어야 합니다.

  const { receiverBank, accountInput } = route.params;
  const appendAmount = (value) => {
    setAmount((prevAmount) => {
      const newAmount = prevAmount * 10 + parseInt(value);
      return Math.min(newAmount, accountBalance);
    });
  };

  const setQuickAmount = (value) => {
    if (value === "전액") {
      setAmount(accountBalance);
    } else {
      const quickValues = {
        "100만": 1000000,
        "10만": 100000,
        "5만": 50000,
        "1만": 10000,
      };
      setAmount(Math.min(quickValues[value], accountBalance));
    }
  };
  //   console.log(receiverAccount + "11receiverAccount");

  // const appendAmount = (value) => {
  //   if (amount === "0원") {
  //     setAmount(`${value}원`);
  //   } else {
  //     setAmount(`${amount.slice(0, -1)}${value}원`);
  //   }
  // };

  // const removeLast = () => {
  //   if (amount.length > 2) {
  //     setAmount(`${amount.slice(0, -2)}`);
  //   } else {
  //     setAmount("0원");
  //   }
  // };

  const formatCurrency = (value) => {
    return value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",") + "원";
  };

  // const appendAmount = (value) => {
  //   setAmount((prevAmount) => prevAmount * 10 + parseInt(value));
  // };

  const removeLast = () => {
    setAmount((prevAmount) => Math.floor(prevAmount / 10));
  };

  return (
    <View style={styles.container}>
      <HeaderScreen navigation={navigation} title="이체" />

      <Text style={styles.amountText}>{formatCurrency(amount)}</Text>
      <Text style={styles.balanceText}>
        출금 가능 금액: {formatCurrency(accountBalance)}
      </Text>

      <View style={styles.quickSelect}>
        {["100만", "10만", "5만", "1만", "전액"].map((quickAmount) => (
          <TouchableOpacity
            key={quickAmount}
            style={styles.quickSelectButton}
            onPress={() => setQuickAmount(quickAmount)}
          >
            <Text>{quickAmount}</Text>
          </TouchableOpacity>
        ))}
      </View>

      <View style={styles.numPad}>
        {[
          ["1", "2", "3"],
          ["4", "5", "6"],
          ["7", "8", "9"],
          ["", "0", "<-"],
        ].map((row, rowIndex) => (
          <View key={rowIndex} style={styles.numRow}>
            {row.map((num) => (
              <TouchableOpacity
                key={num}
                style={styles.numButton}
                onPress={() => {
                  if (num === "<-") {
                    removeLast();
                  } else if (num !== "") {
                    appendAmount(num);
                  }
                }}
              >
                <Text style={styles.numText}>{num}</Text>
              </TouchableOpacity>
            ))}
          </View>
        ))}
      </View>

      <TouchableOpacity
        style={styles.confirmButton}
        onPress={() => {
          navigation.navigate("TransferDetailScreen", {
            amount: amount,
            receiverBank: receiverBank,
            receiverAccount: accountInput,
          });
        }}
      >
        <Text style={styles.confirmText}>확인</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
  },

  amountText: {
    fontSize: 36,
    textAlign: "center",
    marginVertical: 20,
  },
  balanceText: {
    fontSize: 16,
    textAlign: "center",
    marginBottom: 50,
  },
  quickSelect: {
    flexDirection: "row",
    justifyContent: "space-between",
    marginBottom: 20,
  },
  quickSelectButton: {
    padding: 10,
  },
  numPad: {
    flex: 1,
    justifyContent: "center",
  },
  numRow: {
    flexDirection: "row",
    justifyContent: "space-between",
    marginBottom: 20,
  },
  numButton: {
    width: width / 3 - 40,
    height: 60,
    justifyContent: "center",
    alignItems: "center",
    // borderWidth: 1,
    borderColor: "#e0e0e0",
    borderRadius: 5,
  },
  numText: {
    fontSize: 40,
  },
  confirmButton: {
    marginLeft: -30,
    marginRight: -30,
    backgroundColor: "purple",
    padding: 15,
    alignItems: "center",
    borderRadius: 5,
  },
  confirmText: {
    fontSize: 18,
    color: "white",
  },
});
