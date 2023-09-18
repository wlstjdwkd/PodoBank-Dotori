import React from 'react';
import { 
  View,
  Text,
  Image,
  StyleSheet,
  ScrollView,
  TouchableOpacity,
  FlatList,
} from 'react-native';

const banks = [
  {
    id: "1",
    name: "포도은행",
    image: require('../../assets/images/logo_podo.png'),
  },
  {
    id: "2",
    name: "국민은행",
    image: require('../../assets/images/logo_podo.png'),
  },
  {
    id: "3",
    name: "카카오뱅크",
    image: require('../../assets/images/logo_podo.png'),
  },
  {
    id: "4",
    name: "신한은행",
    image: require('../../assets/images/logo_podo.png'),
  },
]

export default function OneCent1Screen({ navigation }) {
  return (
    <ScrollView contentContainerStyle={styles.container}>
      <Text style={styles.boldText}>인증을 하기 위한</Text>
      <Text style={styles.boldText}>은행을 선택하세요.</Text>
      <Text style={styles.grayText}>
        1원 인증을 진행할 은행을 선택해주세요.
      </Text>

      <View style={styles.bankContainer}>
        {banks.map((bank) => (
          <TouchableOpacity
            key={bank.id}
            style={styles.bankBox}
            onPress={() => navigation.navigate('OneCent2Screen')}
          >
            <Image style={styles.bankLogo} source={bank.image} />
            <Text style={styles.bankName}>{bank.name}</Text>
          </TouchableOpacity>
        ))}
      </View>
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'flex-start',
    justifyContent: 'center',
    backgroundColor: 'white',
    padding: 16,
  },
  boldText: {
    fontWeight: 'bold',
    fontSize: 20,
    marginBottom: 8,
    marginLeft: 5,
    textAlign: 'left',
  },
  grayText: {
    color: '#A9A9A9',
    fontSize: 10,
    marginBottom: 16,
  
    marginLeft: 5,
    textAlign: 'left',
  },
  bankContainer: {
    flexDirection: 'row',
    flexWrap: 'wrap',
  },
  bankBox: {
    width: '30%',
    backgroundColor: '#EEEEEE',
    borderRadius: 13,
    margin: 4,
    padding: 8,
    alignItems: 'center',
  },
  bankLogo: {
    width: 40,
    height: 40,
  },
  bankName: {
    marginTop: 8,
    textAlign: 'center',
  },
});
