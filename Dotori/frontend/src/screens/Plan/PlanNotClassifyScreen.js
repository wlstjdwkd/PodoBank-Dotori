import React, { useState } from "react";
import {
  View,
  Text,
  StyleSheet,
  TouchableOpacity,
  ScrollView,
  FlatList,
} from "react-native";
import HeaderComponent from "../Components/HeaderScreen";

export default function PlanNotClassifyScreen({ navigation }) {
  // TODO: 서버에서 데이터를 가져와 아래 변수들을 설정하세요
  const sampleData = {
    accountName: "시민주의 통장",
    notClassifyGroup: [
      {
        categoryGroupName: "냥냥이 식비",
        categoryList: [
          {
            transaction_at: "2023.08.12 16:30",
            transaction_detail: "배달의 민족",
            amount: 10000,
          },
        ],
      },
      {
        categoryGroupName: "배달비",
        categoryList: [
          {
            transaction_at: "2023.08.12 16:30",
            transaction_detail: "배달의 민족",
            amount: 10000,
          },
          {
            transaction_at: "2023.08.13 17:30",
            transaction_detail: "요기요",
            amount: 10000,
          },
          {
            transaction_at: "2023.08.15 19:30",
            transaction_detail: "배달의 민족",
            amount: 10000,
          },
        ],
      },
    ],
  };
  const [selectedGroupName, setSelectedGroupName] = useState(
    sampleData.notClassifyGroup[0]?.categoryGroupName || null
  );

  const formatNumber = (num) =>
    num.toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,");
  const getCategoryItems = () =>
    sampleData.notClassifyGroup.find(
      (group) => group.categoryGroupName === selectedGroupName
    )?.categoryList || [];

  return (
    <View style={styles.container}>
      <HeaderComponent
        title="미분류 항목"
        cancelNavi="PlanMainScreen"
        navigation={navigation}
      ></HeaderComponent>
      <View style={styles.innerContainer}>
        <Text style={styles.accountName}>{sampleData.accountName}</Text>
        <ScrollView horizontal showsHorizontalScrollIndicator={false}>
          {sampleData.notClassifyGroup.map((group) => (
            <TouchableOpacity
              key={group.categoryGroupName}
              onPress={() => setSelectedGroupName(group.categoryGroupName)}
              style={
                selectedGroupName === group.categoryGroupName
                  ? styles.selectedGroupName
                  : styles.groupName
              }
            >
              <Text
                style={
                  selectedGroupName === group.categoryGroupName
                    ? styles.selectedGroupText
                    : styles.groupText
                }
              >
                {group.categoryGroupName}
              </Text>
            </TouchableOpacity>
          ))}
        </ScrollView>
        <View style={styles.divider} />
        <FlatList
          data={getCategoryItems()}
          renderItem={({ item }) => (
            <View style={styles.itemContainer}>
              <View style={styles.itemLeftContainer}>
                <Text style={styles.transactionDetail}>
                  {item.transaction_detail}
                </Text>
                <View style={styles.transactionInfo}>
                  <Text style={styles.transactionAt}>
                    {item.transaction_at}
                  </Text>
                  <Text style={styles.amount}>
                    {formatNumber(item.amount)}원
                  </Text>
                </View>
              </View>
              {/* <View style={styles.categoryBox}>
                <Text style={styles.categoryText}>카테고리</Text>
              </View> */}
            </View>
          )}
          keyExtractor={(item, index) => index.toString()}
        />
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    // alignItems: "center",
    backgroundColor: "white",
    padding: 16,
  },
  innerContainer: {
    // alignItems: "center",
  },
  accountName: {
    fontWeight: "bold",
    alignSelf: "flex-start",
    fontSize: 24,
    marginLeft: 16,
    marginTop: 20,
    marginBottom: 20,
  },
  groupName: {
    padding: 10,
  },

  selectedGroupName: {
    borderBottomWidth: 5,
    borderBottomColor: "#FF965C",
    padding: 10,
  },
  groupText: {
    color: "#727070",
  },
  selectedGroupText: {
    color: "#FF965C",
  },
  divider: {
    height: 1,
    backgroundColor: "#F5F5F5",
    width: "100%",
    marginVertical: 10,
  },
  itemContainer: {
    flexDirection: "row",
    // justifyContent: "space-between",
    alignItems: "center",
    padding: 10,
    borderColor: "#E3E3E3",
    borderWidth: 1,
    borderRadius: 15,
    marginVertical: 5,
    marginLeft: 20,

    width: "90%",
  },
  itemLeftContainer: {
    flex: 1,
    justifyContent: "space-between",
    marginLeft: 10,
    marginRight: 10,
  },
  transactionInfo: {
    flexDirection: "row",
    justifyContent: "space-between",
    marginTop: 5,
  },
  transactionDetail: {
    flex: 1,
    fontWeight: "bold",
    marginBottom: 10,
  },
  categoryBox: {
    backgroundColor: "#F2F5F8",
    borderRadius: 10,
    padding: 5,
    marginTop: -20,
  },
  transactionAt: {
    color: "#7D7D7D",
  },
  amount: {
    textAlign: "right",
  },
  categoryText: {
    fontSize: 10,
  },
});
