import React from "react";
import { View, Text, StyleSheet, Button, TouchableOpacity } from "react-native";

export default function TransactionDetailScreen({ navigation }) {
  const isWithdrawal = true; // 예제에서는 출금으로 가정
  return (
    <View style={styles.container}>
      <View style={styles.header}>
        <Text style={{ flex: 1 }}></Text>
        <Text
          style={{
            flex: 2,
            textAlign: "center",
            fontWeight: "bold",
            fontSize: 16,
          }}
        >
          거래내역상세
        </Text>
        <TouchableOpacity
          style={{ flex: 1, alignItems: "flex-end" }}
          onPress={() => navigation.goBack()}
        >
          <Text style={{ fontSize: 20 }}>X</Text>
        </TouchableOpacity>
      </View>
      <Text style={styles.boldText}>고용노동부구미시청</Text>
      <View style={styles.separator} />

      <Text style={styles.date}>2023.08.29 12:46:25</Text>
      <View style={styles.transactionRow}>
        <Text>거래금액</Text>
        <View style={{ flexDirection: "row" }}>
          <Text>{isWithdrawal ? "출금 " : "입금 "}</Text>
          <Text style={isWithdrawal ? styles.redText : styles.blueText}>
            316,900
          </Text>
          <Text>원</Text>
        </View>
      </View>
      <View style={styles.transactionRow}>
        <Text>거래 후 잔액</Text>
        <Text>0원</Text>
      </View>
      <View style={styles.transactionRow}>
        <Text>거래유형</Text>
        <Text>자동이체</Text>
      </View>

      <View style={styles.transactionRow}>
        <Text>내 통장 표시</Text>
        <Text>고용노동부구미시청</Text>
      </View>

      <TouchableOpacity
        style={styles.confirmButton}
        onPress={() => navigation.goBack()}
      >
        <Text style={styles.confirmButtonText}>확인</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 16,
  },
  header: {
    flexDirection: "row",
    alignItems: "center",
    paddingBottom: 10,
    marginBottom: 30,
    marginTop: 15,
  },
  boldText: {
    fontWeight: "bold",
    marginBottom: 30,
  },
  separator: {
    borderBottomWidth: 2,
    marginVertical: 10,
    marginBottom: 20,
  },
  date: {
    marginBottom: 20,
  },
  transactionRow: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
    marginVertical: 5,
    marginBottom: 10,
  },
  redText: {
    color: "red",
  },
  blueText: {
    color: "blue",
  },
  rightAlignedMultiLineText: {
    alignItems: "flex-end",
  },
  confirmButton: {
    paddingVertical: 10,
    paddingHorizontal: 20,
    backgroundColor: "#842DC480",
    borderRadius: 5,
    alignItems: "center",
    marginTop: 60,
  },
  confirmButtonText: {
    color: "white",
    fontWeight: "bold",
    fontSize: 16,
  },
});
