package com.samyotech.petstand.utils;


public class Utils {
   /* int currentOffset;

    public static final List<Friend> friends = new ArrayList<>();

//    static {
//        friends.add(new Friend(R.drawable.dog_one, "ANASTASIA", R.color.sienna, "Sport", "Literature", "Music", "Art", "Technology"));
//        friends.add(new Friend(R.drawable.dog_two, "IRENE", R.color.saffron, "Travelling", "Flights", "Books", "Painting", "Design"));
//        friends.add(new Friend(R.drawable.dog_three, "KATE", R.color.green, "Sales", "Pets", "Skiing", "Hairstyles", "Сoffee"));
//        friends.add(new Friend(R.drawable.dog_four, "PAUL", R.color.pink, "Android", "Development", "Design", "Wearables", "Pets"));
//        friends.add(new Friend(R.drawable.dog_five, "DARIA", R.color.orange, "Design", "Fitness", "Healthcare", "UI/UX", "Chatting"));
//        friends.add(new Friend(R.drawable.dog_six, "KIRILL", R.color.saffron, "Development", "Android", "Healthcare", "Sport", "Rock Music"));
//        friends.add(new Friend(R.drawable.dog_seven, "JULIA", R.color.green, "Cinema", "Music", "Tatoo", "Animals", "Management"));
//        friends.add(new Friend(R.drawable.dog_eight, "YALANTIS", R.color.purple, "Android", "IOS", "Application", "Development", "Company"));
//    }

    public List<DemoItem> moarItems(ArrayList<GalleryDTO> galleryList) {
        List<DemoItem> items = new ArrayList<>();

        for (int i = 0; i < galleryList.size(); i++) {
            int colSpan = Math.random() < 0.2f ? 2 : 1;
            // Swap the next 2 lines to have items with variable
            // column/row span.
            // int rowSpan = Math.random() < 0.2f ? 2 : 1;
            int rowSpan = colSpan;
            DemoItem item = new DemoItem(colSpan, rowSpan, currentOffset + i);
            item.setUser_profile_pic(galleryList.get(i).getUser_profile_pic());
            item.setTitle(galleryList.get(i).getTitle());
            item.setGallery_id(galleryList.get(i).getGallery_id());
            item.setImage_path(galleryList.get(i).getImage_path());
            item.setDescription(galleryList.get(i).getDescription());
            items.add(item);
        }

        currentOffset += galleryList.size();

        return items;
    }
    public static String getQuery(String params) throws UnsupportedEncodingException {
        try {
            String val = URLEncoder.encode(params, "UTF-8");
            return val;
        } catch (Exception e) {
        }
        return "";
    }*/
}
